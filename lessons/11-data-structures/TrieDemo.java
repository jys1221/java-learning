import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

/**
 * 知识点：字典树（Trie：前缀树，插入 / 整词查找 / 前缀查找 / 自动补全）
 * ============================================================
 * 一句话：Trie 把单词按字母拆开，沿"字符路径"挂在树上；同一前缀的单词共享前面的路径，
 *         查前缀、做自动补全时不用逐个单词全串比较，沿树走一遍即可。
 *
 * 为什么需要它：
 *   搜索引擎输入联想、输入法候选、通讯录按名字过滤、路由前缀匹配，都是"按前缀找词"。
 *   用普通字符串集合要对每个词做 startsWith，O(n·L)；Trie 按字母走树，查询只和词长 L 相关。
 *
 * 复杂度速查（L = 单词长度，n = 单词个数）：
 *   操作              平均 / 最坏      说明
 *   插入 insert       O(L)           每个字母走一层
 *   整词查找 search    O(L)           走到对应节点且 isEnd 为真
 *   前缀查找 startsWith O(L)          只要前缀路径走通即可，不要求 isEnd
 *   自动补全           O(L + k)       k 为补全出的单词个数，需 DFS 收集后缀
 *
 * 易错点：
 *   1. isEnd 标记"这里是一个完整单词"；前缀节点不一定是词（如 "ca" 是 "cat" 的前缀但不是词）。
 *   2. 查整词必须同时满足"路径走通"和"终点 isEnd=true"；只走通路径只说明是前缀。
 *   3. 子节点数组长度按字符集定：英文小写 26；若含大写、数字、中文，要换 Map 子节点或更大数组。
 *   4. Trie 用空间换时间：公共前缀只存一份，但共享路径之外的分支会占额外指针。
 *   5. 单词为空串时要单独处理；本文件约定插入的都是非空小写字母词。
 *
 * 与 Java 自带类的对应关系：
 *   JDK 没有内置 Trie；可借助 TreeMap/HashMap 实现子节点映射。本文件用 26 路数组手写，
 *   自动补全结果与"用 String.startsWith 过滤集合"的朴素做法对拍一致。
 *
 * 一句话口诀：
 *   一字母一层路，同前缀共享路径；isEnd 才算整词，前缀走通即可补全。
 *
 * 运行方式（在本文件所在目录）：
 *   javac -encoding UTF-8 TrieDemo.java
 *   java TrieDemo
 */
public class TrieDemo {

    /** 断言通过计数 */
    private static int passed = 0;

    public static void main(String[] args) {

        System.out.println("========== 1. 插入、整词查找与前缀查找 ==========");
        Trie trie = new Trie();
        String[] words = {"cat", "car", "card", "cake", "dog", "do"};
        for (String w : words) {
            trie.insert(w);
        }

        check(trie.search("cat"), "整词查找 cat 存在");
        check(trie.search("card"), "整词查找 card 存在");
        check(!trie.search("ca"), "ca 只是前缀，不是整词，search 返回 false");
        check(trie.startsWith("ca"), "前缀 ca 存在");
        check(trie.startsWith("dog"), "前缀 dog 存在");
        check(!trie.startsWith("xyz"), "前缀 xyz 不存在");

        System.out.println();
        System.out.println("========== 2. 自动补全：收集某前缀开头的所有词 ==========");
        List<String> acCa = trie.autocomplete("ca");
        List<String> acDo = trie.autocomplete("do");
        System.out.println("  前缀 ca 补全：" + acCa);
        System.out.println("  前缀 do 补全：" + acDo);
        check(acCa.equals(List.of("cake", "car", "card", "cat")), "ca 补全出 cake/car/card/cat");
        check(acDo.equals(List.of("do", "dog")), "do 补全出 do/dog");

        System.out.println();
        System.out.println("========== 3. 与朴素 startsWith 过滤对拍 ==========");
        Set<String> wordSet = new HashSet<>(List.of(words));
        for (String prefix : List.of("ca", "do", "c", "d", "x")) {
            List<String> trieResult = trie.autocomplete(prefix);
            List<String> plainResult = new ArrayList<>();
            for (String w : wordSet) {
                if (w.startsWith(prefix)) {
                    plainResult.add(w);
                }
            }
            java.util.Collections.sort(trieResult);
            java.util.Collections.sort(plainResult);
            System.out.println("  前缀 " + prefix + "：Trie=" + trieResult + "，朴素=" + plainResult);
            check(trieResult.equals(plainResult), "前缀 " + prefix + " 的补全结果与 startsWith 过滤一致");
        }

        System.out.println();
        System.out.println("========== 4. 固定种子实测：Trie 前缀查询 vs 朴素扫描 ==========");
        demoPerformance();

        System.out.println();
        System.out.println("全部断言通过，共 " + passed + " 条。");
    }

    // =========================================================
    // 手写 Trie
    // =========================================================
    static class Trie {

        static class Node {
            /** 26 个小写字母子节点；null 表示该字母分支不存在 */
            Node[] children = new Node[26];
            /** 该节点是否是一个完整单词的结尾 */
            boolean isEnd;
        }

        private final Node root = new Node();

        /** 字母 a~z 映射到 0~25 */
        private int index(char c) {
            return c - 'a';
        }

        /** 插入单词：逐字母走树，没有分支就新建 */
        void insert(String word) {
            Node cur = root;
            for (int i = 0; i < word.length(); i++) {
                int idx = index(word.charAt(i));
                if (cur.children[idx] == null) {
                    cur.children[idx] = new Node();
                }
                cur = cur.children[idx];
            }
            cur.isEnd = true; // 走到终点，标记这是一个完整单词
        }

        /** 整词查找：路径走通且终点 isEnd 为真 */
        boolean search(String word) {
            Node node = walk(word);
            return node != null && node.isEnd;
        }

        /** 前缀查找：路径走通即可，不要求 isEnd */
        boolean startsWith(String prefix) {
            return walk(prefix) != null;
        }

        /** 沿单词走到对应节点；中途断链返回 null */
        private Node walk(String word) {
            Node cur = root;
            for (int i = 0; i < word.length(); i++) {
                int idx = index(word.charAt(i));
                if (cur.children[idx] == null) {
                    return null;
                }
                cur = cur.children[idx];
            }
            return cur;
        }

        /** 自动补全：先走到前缀节点，再 DFS 收集其后缀拼成完整单词 */
        List<String> autocomplete(String prefix) {
            List<String> result = new ArrayList<>();
            Node node = walk(prefix);
            if (node == null) {
                return result;
            }
            dfsCollect(node, prefix, result);
            return result;
        }

        /** DFS：当前路径 current 走到 node；遇到 isEnd 就把 current 收入结果 */
        private void dfsCollect(Node node, String current, List<String> result) {
            if (node.isEnd) {
                result.add(current);
            }
            for (int i = 0; i < 26; i++) {
                if (node.children[i] != null) {
                    char c = (char) ('a' + i);
                    dfsCollect(node.children[i], current + c, result);
                }
            }
        }
    }

    // ---------------------------------------------------------
    // 实测：固定种子生成一批单词，对比 Trie 与朴素扫描
    // ---------------------------------------------------------
    private static void demoPerformance() {
        final int n = 2000;
        Random rnd = new Random(20261128);
        String[] pool = {"a", "b", "c", "d", "e", "f", "g", "h"};

        List<String> words = new ArrayList<>();
        Trie trie = new Trie();
        for (int i = 0; i < n; i++) {
            int len = 3 + rnd.nextInt(4); // 长度 3~6
            StringBuilder sb = new StringBuilder();
            for (int j = 0; j < len; j++) {
                sb.append(pool[rnd.nextInt(pool.length)]);
            }
            String w = sb.toString();
            words.add(w);
            trie.insert(w);
        }

        // Trie 前缀查询：对若干常见前缀走树 + 收集
        long t1 = System.nanoTime();
        int trieHits = 0;
        for (String prefix : words) {
            String p = prefix.substring(0, 2);
            trieHits += trie.autocomplete(p).size();
        }
        long trieMs = (System.nanoTime() - t1) / 1_000_000;

        // 朴素做法：每个前缀对去重后的单词集合做 startsWith（Trie 本身按去重存储）
        Set<String> uniqueWords = new HashSet<>(words);
        long t2 = System.nanoTime();
        int plainHits = 0;
        for (String prefix : words) {
            String p = prefix.substring(0, 2);
            for (String w : uniqueWords) {
                if (w.startsWith(p)) {
                    plainHits++;
                }
            }
        }
        long plainMs = (System.nanoTime() - t2) / 1_000_000;

        System.out.println("  单词 " + n + " 个；Trie 补全累计命中 " + trieHits + "，耗时 " + trieMs + " ms");
        System.out.println("  朴素扫描累计命中 " + plainHits + "，耗时 " + plainMs + " ms");
        check(trieHits == plainHits, "两种口径补全命中总数一致");
        check(trie.startsWith(words.get(0).substring(0, 2)), "首个单词的前缀在 Trie 中存在");
    }

    // =========================================================
    // 断言小工具
    // =========================================================
    private static void check(boolean condition, String what) {
        if (!condition) {
            throw new AssertionError("校验失败：" + what);
        }
        passed++;
        System.out.println("  [通过] " + what);
    }
}
