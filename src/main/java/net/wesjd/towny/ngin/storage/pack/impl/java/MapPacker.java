package net.wesjd.towny.ngin.storage.pack.impl.java;

import com.google.inject.Inject;
import net.wesjd.towny.ngin.storage.InheritSuperPacker;
import net.wesjd.towny.ngin.storage.pack.Packer;
import net.wesjd.towny.ngin.storage.pack.PackerStore;
import org.msgpack.core.MessagePacker;
import org.msgpack.core.MessageUnpacker;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class MapPacker extends Packer<Map> {

    @Inject
    private PackerStore packerStore;

    @SuppressWarnings("unchecked")
    @Override
    public void packup(Map packing, MessagePacker packer) throws IOException {
        packer.packArrayHeader(packing.size());

        Packer keyPacker = null, valuePacker = null;
        for (Map.Entry<Object, Object> entry : ((Map<Object, Object>) packing).entrySet()) {
            if (keyPacker == null) {
                Class key = entry.getKey().getClass(), value = entry.getValue().getClass();

                while (key.isAnnotationPresent(InheritSuperPacker.class)) key = key.getSuperclass();
                while (value.isAnnotationPresent(InheritSuperPacker.class)) value = value.getSuperclass();

                Class finalKey = key, finalValue = value;
                keyPacker = packerStore.lookup(key).orElseThrow(() -> new RuntimeException("Unable to find packer key type " + finalKey));
                valuePacker = packerStore.lookup(value).orElseThrow(() -> new RuntimeException("Unable to find packer for value type " + finalValue));

                packer.packString(key.getName());
                packer.packString(value.getName());
            }

            keyPacker.packup(entry.getKey(), packer);
            valuePacker.packup(entry.getValue(), packer);
        }
    }

    @Override
    public Map unbox(MessageUnpacker unpacker) throws IOException {
        int reading = unpacker.unpackArrayHeader();
        Map<Object, Object> unpacking = new HashMap<>();
        Packer keyUnpacker = null, valueUnpacker = null;
        for (int i = 0; i < reading; i++) {
            if (keyUnpacker == null) {
                String key = unpacker.unpackString(), value = unpacker.unpackString();
                try {
                    keyUnpacker = packerStore.lookup(Class.forName(key)).orElseThrow(() -> new RuntimeException("Unable to find packer for key type " + key));
                } catch (ClassNotFoundException e) {
                    throw new RuntimeException("Unable to find key class " + key);
                }

                try {
                    valueUnpacker = packerStore.lookup(Class.forName(value)).orElseThrow(() -> new RuntimeException("Unable to find packer for value type " + value));
                } catch (ClassNotFoundException e) {
                    throw new RuntimeException("Unable to find value class " + value);
                }
            }
            unpacking.put(keyUnpacker.unbox(unpacker), valueUnpacker.unbox(unpacker));
        }
        return unpacking;
    }

}
