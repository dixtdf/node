package io.github.dixtdf.node.utils;

import io.github.dixtdf.node.support.NodeFunction;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 森林节点合并工具
 *
 * @author 杨毅
 */
public class ForestNodeMerger<T> implements Serializable {

    /**
     * 将节点数组归并为一个森林（多棵树）（填充节点的children域） 时间复杂度为O(n^2)
     *
     * @param items    节点域
     * @param key      节点主键
     * @param parent   节点父级
     * @param children 子节点集合
     * @return 多棵树的根节点集合
     */
    public List<T> merge(List<T> items, NodeFunction<T, ?> key, NodeFunction<T, ?> parent, NodeFunction<T, ?> children) {
        return merge(items, key, parent, children, null);
    }

    /**
     * 将节点数组归并为一个森林（多棵树）（填充节点的children域） 时间复杂度为O(n^2)
     *
     * @param items    节点域
     * @param key      节点主键
     * @param parent   节点父级
     * @param children 子节点集合
     * @param rootKey  根节点
     * @return 多棵树的根节点集合
     */
    public List<T> merge(List<T> items, NodeFunction<T, ?> key, NodeFunction<T, ?> parent, NodeFunction<T, ?> children, String... rootKey) {
        List<String> rootKeyList = Arrays.asList(rootKey);
        ForestNodeManager<T> forestNodeManager = new ForestNodeManager<>(items, key);
        if (ArrayUtils.isNotEmpty(rootKey)) {
            forestNodeManager.addParentKeys(rootKeyList);
        }
        items.forEach(forestNode -> {
            // 查询父节点
            try {
                T t = forestNodeManager.getTreeNodeAT(((String) forestNode.getClass().getMethod(new NodeLambdaWrapper<T>().getColumn(parent)).invoke(forestNode)));
                if (t != null) {
                    // 作为子节点保存
                    Collection<T> c = (Collection<T>) t.getClass().getMethod(new NodeLambdaWrapper<T>().getColumn(children)).invoke(t);
                    if (c == null) {
                        c = new ArrayList<>();
                    }
                    c.add(forestNode);
                } else {
                    // 将父节点存储
                    forestNodeManager.addParentId((String) forestNode.getClass().getMethod(new NodeLambdaWrapper<T>().getColumn(key)).invoke(forestNode));
                }
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            } catch (InvocationTargetException e) {
                throw new RuntimeException(e);
            } catch (NoSuchMethodException e) {
                throw new RuntimeException(e);
            }
        });
        return forestNodeManager.getRoot().stream().filter(a -> {
            try {
                String b = (String) a.getClass().getMethod(new NodeLambdaWrapper<T>().getColumn(key)).invoke(a);
                return rootKeyList.stream().anyMatch(c -> StringUtils.equals(c, b));
            } catch (NoSuchMethodException e) {
                throw new RuntimeException(e);
            } catch (InvocationTargetException e) {
                throw new RuntimeException(e);
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        }).collect(Collectors.toList());
    }

}
