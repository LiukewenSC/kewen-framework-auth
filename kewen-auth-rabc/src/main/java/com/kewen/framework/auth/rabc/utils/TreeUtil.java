package com.kewen.framework.auth.rabc.utils;

import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * 树转换工具
 * @author LiuKewen
 * @date 2022/1/6 12:51
 */
public class TreeUtil {
    private TreeUtil() {
    }

    /**
     * 转换树形结构
     * @param source 需要组装的数据
     * @param <P> 第一层数据的parentId，
     *           eg1 id="1" parentId=null ,topParentId=null 则 此数据为第一层数据
     *           eg2 id="1" parentId="i1" ,topParentId=i1 则 此数据为第一层数据
     *           eg3 id="1" parentId="i1" ,topParentId=null 则 此数据不为第一层数据
     * @param <T> 泛型
     * @return 分层之后的列表
     */
    public static <P,T extends TreeBase<T,P>> List<T> transfer(List<T> source,P topParentId){
        List<T> ts = new ArrayList<>(source);
        Iterator<T> iterator = ts.iterator();

        List<T> root = new ArrayList<>();
        //移除根数据
        while (iterator.hasNext()){
            T next = iterator.next();
            if (next.isTop(topParentId)){
                root.add(next);
                iterator.remove();
            }
        }
        /* 检测环路 貌似不需要检测环路
        List<String> combine = ts.stream().map(
                t -> t.getParentId() == null ? t.getId().toString() : t.getId().toString().concat(t.getParentId().toString())
        ).collect(Collectors.toList());
        boolean hasLoop = ts.stream()
                .filter(t -> t.getParentId() != null)
                .anyMatch(t -> combine.contains(t.getParentId().toString().concat(t.getId().toString())));
        if (hasLoop){
            throw new StackOverflowError("数据集有递归,无法组装树形结构");
        }*/

        Map<P, List<T>> parentMap = ts.stream()
                .filter(m->m.getParentId()!=null)
                .collect(Collectors.groupingBy(T::getParentId));
        for (T t : ts) {
            //设置子菜单（引用，因此会将相关数据加入其中，但是不得有循环，否则栈溢出）
            t.setChildren(parentMap.get(t.getId()));
        }
        return root.stream().peek(r -> r.setChildren(parentMap.get(r.getId()))).collect(Collectors.toList());
    }
    /**
     * 将树反向变成列表
     * @param tree 树
     * @param <P> id泛型
     * @param <T> 节点泛型
     * @return
     */
    public static  <P,T extends TreeBase<T,P>> List<T> unTransfer(T tree){
        List<T> list = new ArrayList<>();
        list.add(tree);
        if (!CollectionUtils.isEmpty(tree.getChildren())){
            for (T child : tree.getChildren()) {
                list.addAll(unTransfer(child));
            }
        }
        return list;
    }
    /**
     * 从众多树中取回自己的子树
     * @param trees 树集合
     * @param id id
     * @param <P>
     * @param <T>
     * @return
     */
    public static  <P,T extends TreeBase<T,P>> T fetchSubTree(List<T> trees, P id){
        //循环每一节点
        for (T node : trees) {
            //匹配到了则返回
            if (node.getId().equals(id)){
                return node;
            }
            //没有孩子了则此节点中结束查找，返回上一级查找
            if (CollectionUtils.isEmpty(node.getChildren())){
                continue;
            }
            //遍历子节点，递归查找，查找到了则一直往上返回
            T nodeResp = fetchSubTree(node.getChildren(), id);
            if (nodeResp !=null){
                return nodeResp;
            }
        }
        //一直未找到则此列表树中无
        return null;
    }

    /**
     * 获取子树的ID列表集合
     * @param tree 树
     * @param <ID>
     * @param <T>
     * @return
     */
    public static  <ID,T extends TreeBase<T, ID>> List<ID> fetchSubIds(T tree){
        List<ID> ids = new ArrayList<>();
        ids.add(tree.getId());
        List<T> children = tree.getChildren();
        if (!CollectionUtils.isEmpty(children)){
            for (T child : children) {
                List<ID> subIds = fetchSubIds(child);
                ids.addAll(subIds);
            }
        }
        return ids;
    }


    /**
     *  从一颗整树上面移除掉不满足条件的元素，如果有叶子节点，那么枝条节点是不会被移除的
     * @param collections 需要移除的集合
     * @param predicate 叶子节点元素匹配的条件， 即什么情况下需要移除
     * @param <T>
     */
    public static <T extends TreeChildren<T>> void removeIfUnmatch(Collection<T> collections, Predicate<T> predicate){
        Iterator<T> iterator = collections.iterator();
        while (iterator.hasNext()){
            T next = iterator.next();
            boolean needRemove = removeIfUnmatch(next, predicate);
            if (needRemove){
                iterator.remove();
            }
        }
    }
    /**
     *  从一颗整树上面移除掉不满足条件的元素，如果有叶子节点，那么枝条节点是不会被移除的
     * @param t 需要移除子元素的树
     * @param predicate 叶子节点元素匹配的条件， 即什么情况下需要移除
     * @param <T>
     */
    public static <T extends TreeChildren<T>> boolean removeIfUnmatch(T t, Predicate<T> predicate){
        List<T> children = t.getChildren();
        //有子元素节点优先处理子元素节点
        if (!CollectionUtils.isEmpty(children)){
            removeIfUnmatch(children,predicate);
        }
        //判断还有子集没有，有子集则自己本身不被移除
        if (!CollectionUtils.isEmpty(children)){
            return false;
        } else {
            boolean needRemove = predicate.test(t);
            return needRemove;
        }
    }

    /**
     * 将一个带子集的集合转换为另一个带子集的集合
     *  其中，相同字段用的是BeanUtil.toBean() 做深度克隆转换
     * @param collection
     * @param clazz
     * @param <T>
     * @return
     */
    public static <T extends TreeChildren<T>> List<T> convertList(Collection<? extends TreeChildren> collection, Class<T> clazz){
        ArrayList<T> list = new ArrayList<>();
        for (TreeChildren col : collection) {
            T bean = BeanUtil.toBean(col, clazz);
            if (!CollectionUtils.isEmpty(col.getChildren())){
                bean.setChildren(convertList(col.getChildren(), clazz));
            }
            list.add(bean);
        }
        return list;
    }


    /**
     * 树形对象
     * @author LiuKewen
     * @date 2022/1/6 12:52
     */
    public interface TreeBase<T, ID> extends TreeChildren<T> {

        /**
         * 当前id
         * @return
         */
        ID getId();

        /**
         * 父id
         * @return
         */
        ID getParentId();

        /**
         * 是否是顶级菜单 一般顶级菜单的父菜单为null或者 0 或者自己定义的
         * @param p
         * @return
         */
        default boolean isTop(ID p) {
            ID parentId = this.getParentId();
            //默认父菜单id为空则此菜单id为顶层菜单
            if (parentId == null){
                return true;
            }
            //如果id属于int类型，父菜单为0则为顶层菜单
            if (parentId instanceof Integer && (Integer) parentId ==0){
                return true;
            }
            //如果id属于Long类型，父菜单为0L则为顶层菜单
            if (parentId instanceof Long && (Long) parentId == 0L ){
                return true;
            }
            //否则父菜单等于本id，则为顶层菜单
            return p == parentId;
        }
    }

    /**
     * 包含子元素的树定义
     * @param <T>
     */
    public interface TreeChildren<T>{

        void setChildren(List<T> children);

        List<T> getChildren();
    }

}