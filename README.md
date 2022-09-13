# Node-Tree-Plus
简单的树形
```
/**
 * 将节点数组归并为一个森林（多棵树）（填充节点的children域） 时间复杂度为O(n^2)
 *
 * @param items    节点域
 * @param key      节点主键
 * @param parent   节点父级
 * @param children 子节点集合
 * @return 多棵树的根节点集合
 */
new ForestNodeMerger<User>().merge(list, User::getId, User::getPid, User::getChildren);
```
