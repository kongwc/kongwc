package com.kongwc.tools.common.msg.converter.msgpack;

import java.io.IOException;
import java.util.List;

import com.kongwc.tools.common.exception.NotSupportedException;
import com.kongwc.tools.common.msg.converter.MsgConverter;
import org.apache.commons.codec.binary.Base64;
import org.msgpack.MessagePack;
import org.springframework.stereotype.Component;

/**
 * MQ 消息转换器---msgpack实现
 */
@Component
public class MsgpackConverter implements MsgConverter {

    /**
     * 内部msgpack
     */
    private MessagePack msgpack;

    /**
     * 构造函数
     */
    public MsgpackConverter() {
        msgpack = new MessagePack();
//		msgpack.register(XXX.class);
//		msgpack.register(XXX.class);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <T extends Object> String packObject(T obj) {
        byte[] b = null;
        try {
            b = msgpack.write(obj);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            msgpack.unregister();
        }
        return new String(new Base64().encode(b));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <T extends Object> T unpackObject(String objstr, Class<T> clazz) {
        T t = null;
        byte[] bytes = new Base64().decode(objstr.getBytes());
        try {
            t = msgpack.read(bytes, clazz);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            msgpack.unregister();
        }
        return t;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <T extends Object> String packList(List<T> obj) {
        // TODO 不支持
//		ByteArrayOutputStream out = new ByteArrayOutputStream();
//		Packer packer = msgpack.createPacker(out);
//		byte[] b = null;
//		try {
//			packer.write(obj);
//			b = out.toByteArray();
//		} catch (IOException e) {
//			e.printStackTrace();
//		} finally {
//			msgpack.unregister();
//		}
//		return new String(new Base64().encode(b));
        throw new NotSupportedException("Msgpack has not implemented the method.");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <T extends Object> List<T> unpackList(String objstr, Class<T> clazz) {
        // TODO 不支持
//		Template<T> elementTemplate = msgpack.lookup(clazz);
//		Template<List<T>> listTmpl = Templates.tList(elementTemplate);
//
//		List<T> t = null;
//		byte[] bytes = new Base64().decode(objstr.getBytes());
//		try {
//			t = msgpack.read(bytes, listTmpl);
//		} catch (IOException e) {
//			e.printStackTrace();
//		} finally {
//			msgpack.unregister();
//		}
//		return t;
        throw new NotSupportedException("Msgpack has not implemented the method.");
    }

}
