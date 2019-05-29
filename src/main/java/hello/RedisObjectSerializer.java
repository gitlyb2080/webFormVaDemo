/**
 * 
 */
package hello;
import org.springframework.core.convert.converter.Converter;
import org.springframework.core.serializer.support.DeserializingConverter;
import org.springframework.core.serializer.support.SerializingConverter;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.SerializationException;
/**
 * 虽然以上的代码实现了 Redis 基础的数据操作，但是遗憾的是在 Java 开发领域内必须要考虑一个实际的问题，
 * 那么就是对象 的序列化保存问题，毕竟 Redis 数据库的读写速度是非常快的，但是如果不能够进行对象的存储，
 * 这样的存储意义就不大了，这样 就需要准备一个对象的序列化处理程序类，通过对象的形式进行数据的存储。
 * 如果要想进行 Redis 对象序列化操作则一定要首先准备一个序列化处理程序类，这个程序类有实现要求：
 * */
public class RedisObjectSerializer implements RedisSerializer<Object> {
    // 为了方便进行对象与字节数组的转换，所以应该首先准备出两个转换器
    private Converter<Object, byte[]> serializingConverter = new SerializingConverter();
    private Converter<byte[], Object> deserializingConverter = new DeserializingConverter();
    private static final byte[] EMPTY_BYTE_ARRAY = new byte[0]; // 做一个空数组，不是null

    @Override
    public byte[] serialize(Object obj) throws SerializationException {
        if (obj == null) { // 这个时候没有要序列化的对象出现，所以返回的字节数组应该就是一个空数组
            return EMPTY_BYTE_ARRAY;
        }
        return this.serializingConverter.convert(obj); // 将对象变为字节数组
    }

    @Override
    public Object deserialize(byte[] data) throws SerializationException {
        if (data == null || data.length == 0) { // 此时没有对象的内容信息
            return null;
        }
        return this.deserializingConverter.convert(data);
    }

}
