package club.xtdf.node.utils;

import club.xtdf.node.tree.support.NodeFunction;
import club.xtdf.node.tree.support.SerializedLambda;

import java.io.*;
import java.util.Locale;

/**
 * 节点包装
 *
 * @param <T>
 * @author yangyi
 */
public class NodeLambdaWrapper<T> implements Serializable {

    /**
     * 方法名转字段名，直接copy mp的代码
     * 其实就是按java bean的规范，先把get、set、is前缀去掉，然后第二个字符如果不是大写，就把第一个转小写
     *
     * @param name
     * @return
     */
    public static String methodToProperty(String name) {
        if (name.startsWith("is")) {
            name = name.substring(2);
        } else if (name.startsWith("get") || name.startsWith("set")) {
            name = name.substring(3);
        } else {
            throw new RuntimeException("Error parsing property name '" + name + "'.  Didn't start with 'is', 'get' or 'set'.");
        }

        if (name.length() == 1 || (name.length() > 1 && !Character.isUpperCase(name.charAt(1)))) {
            name = name.substring(0, 1).toLowerCase(Locale.ENGLISH) + name.substring(1);
        }

        return name;
    }

    public String getColumn(NodeFunction<T, ?> function) {
        return deserialize(serialize(function)).getImplMethodName();
    }

    private SerializedLambda deserialize(byte[] bytes) {
        try (ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(bytes)) {
            @Override
            protected Class<?> resolveClass(ObjectStreamClass objectStreamClass) throws IOException, ClassNotFoundException {
                return objectStreamClass.getName().equals(java.lang.invoke.SerializedLambda.class.getName()) ? SerializedLambda.class : super.resolveClass(objectStreamClass);
            }
        }) {
            return (SerializedLambda) ois.readObject();
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    private byte[] serialize(NodeFunction function) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream(1024);
        try (ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream)) {
            objectOutputStream.writeObject(function);
            objectOutputStream.flush();
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
        return byteArrayOutputStream.toByteArray();
    }

}
