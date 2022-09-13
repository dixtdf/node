package club.xtdf.node.utils;

import club.xtdf.node.tree.support.NodeFunction;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * 森林节点管理类
 *
 * @param <T>
 * @author yangyi
 */
public class ForestNodeManager<T> {

    /**
     * 森林的所有节点
     * Key INode。id
     */
    private Map<String, Integer> index = new HashMap<String, Integer>();

    private List<T> node;

    /**
     * 森林的父节点ID
     */
    private List<String> parentIds = new ArrayList<String>();

    public ForestNodeManager(List<T> items, NodeFunction<T, ?> key) {
        for (int i = 0; i < items.size(); i++) {
            try {
                T t = items.get(i);
                String value = (String) t.getClass().getMethod(new NodeLambdaWrapper<T>().getColumn(key)).invoke(t);
                index.put(value, i);
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            } catch (InvocationTargetException e) {
                throw new RuntimeException(e);
            } catch (NoSuchMethodException e) {
                throw new RuntimeException(e);
            }
        }
        this.node = items;
    }

    /**
     * 根据节点ID获取一个节点
     *
     * @param id 节点ID
     * @return 对应的节点对象
     */
    public T getTreeNodeAT(String id) {
        Integer in = index.get(id);
        if (in == null) {
            return null;
        }
        return node.get(in);
    }

    /**
     * 增加父节点ID
     *
     * @param parentId 父节点ID
     */
    public void addParentId(String parentId) {
        parentIds.add(parentId);
    }

    /**
     * 增加父节点ID
     *
     * @param parentKeys
     */
    public void addParentKeys(List<String> parentKeys) {
        parentIds.addAll(parentKeys);
    }

    /**
     * 获取树的根节点(一个森林对应多颗树)
     *
     * @return 树的根节点集合
     */
    public List<T> getRoot() {
        return parentIds.parallelStream()
                .flatMap((key) -> Stream.of(getTreeNodeAT(key)))
                .filter((data) -> data != null)
                .collect(Collectors.toList());
    }

}
