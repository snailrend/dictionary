package com.github.snailrend.dictionary.api;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * 字典数据加载接口
 * @author snailrend
 */
public interface DictionaryDataLoader {
    List<DictionaryItem> load(String dictionaryName, LoadParams loadParams);

    /**
     * 加载参数对象
     * 此参数对象是为了适应字典数据量大的情况，没必要将所有数据都加载进来，只需要部分需要翻译的数据即可
     * 假若${@link LoadParams#isEmpty()} 返回结果为 true，则表示加载全部数据
     * 若是评估数据量不大，也可忽略本参数，加载全部数据
     */
    public static class LoadParams {
        /**
         * 加载的字典数据中，字典值必须在此集合中存在
         */
        private Set<String> values = new HashSet<>();
        /**
         * 加载的字典数据中，字典名必须在此集合中存在
         */
        private Set<String> names = new HashSet<>();

        public LoadParams() {
        }

        public Set<String> getValues() {
            return values;
        }

        public void setValues(Set<String> values) {
            this.values = values;
        }

        public Set<String> getNames() {
            return names;
        }

        public void setNames(Set<String> names) {
            this.names = names;
        }

        public boolean isEmpty() {
            return getValues().isEmpty() && getNames().isEmpty();
        }
    }

}
