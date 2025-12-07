package com.kmo.kome.utils;

import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Post 工具类
 * <p>
 * 提供与文章相关的实用工具方法，包括基于 Markdown 文本内容的阅读时间估算。
 */
@Component
public class PostUtils {

    // 正则们（提前编译，性能好）
    private static final Pattern CHINESE_PATTERN = Pattern.compile("[\\u4e00-\\u9fff]");
    private static final Pattern ENGLISH_WORD_PATTERN = Pattern.compile("\\b[a-zA-Z]+\\b");
    private static final Pattern IMAGE_PATTERN = Pattern.compile("!\\[[^]]*\\]\\([^)]*\\)");  // ![alt](url)
    private static final Pattern CODE_BLOCK_PATTERN = Pattern.compile("```[\\s\\S]*?```");     // ```代码```
    private static final Pattern INLINE_CODE_PATTERN = Pattern.compile("`[^`]+`");             // `inline`
    private static final Pattern MATH_BLOCK_PATTERN = Pattern.compile("\\$\\$[^$]+\\$\\$");    // $$公式$$
    private static final Pattern MATH_INLINE_PATTERN = Pattern.compile("(?<!\\$)\\$[^\n$]+\\$(?!\\$)"); // $公式$
    private static final Pattern TABLE_ROW_PATTERN = Pattern.compile("^\\|.*\\|$", Pattern.MULTILINE); // | 表格行 |
    private static final Pattern LIST_ITEM_PATTERN = Pattern.compile("^(?:[-*+•]|\\d+\\.)\\s+.*$", Pattern.MULTILINE); // - 项目 或 1. 项目


    /**
     * 根据给定的 Markdown 内容计算预计阅读时间，并返回以分钟为单位的结果。
     * 阅读时间的计算基于文本内容的各个组成部分，包括中文字符、英文单词、图片、代码块、
     * 数学公式、表格以及列表条目等。
     * 如果内容为空或无效，默认返回 1 分钟。
     *
     * @param markdownContent 要计算阅读时间的 Markdown 内容
     * @return 预计的阅读时间，单位为分钟（至少为 1 分钟）
     */
    public int calculateReadTime(String markdownContent) {
        if (!StringUtils.hasText(markdownContent)) {
            return 1;
        }

        // 1. 统计中文字符数
        Matcher chineseMatcher = CHINESE_PATTERN.matcher(markdownContent);
        int chineseCount = 0;
        while (chineseMatcher.find()) chineseCount++;

        // 2. 统计英文单词数（去掉代码块后再统计更准）
        String textWithoutCode = markdownContent
                .replaceAll("```[\\s\\S]*?```", "")  // 先去掉代码块
                .replaceAll("`[^`]+`", "");          // 再去掉行内代码
        Matcher englishMatcher = ENGLISH_WORD_PATTERN.matcher(textWithoutCode);
        int englishWordCount = 0;
        while (englishMatcher.find()) englishWordCount++;

        // 3. 统计各种耗时元素
        int imageCount = countPattern(markdownContent, IMAGE_PATTERN);
        int codeBlockLineCount = countLinesInPattern(markdownContent);
        int mathBlockCount = countPattern(markdownContent, MATH_BLOCK_PATTERN) + countPattern(markdownContent, MATH_INLINE_PATTERN);
        int tableRowCount = countPattern(markdownContent, TABLE_ROW_PATTERN);
        int listItemCount = countPattern(markdownContent, LIST_ITEM_PATTERN);

        // 4. 计算总阅读时间（秒）
        double seconds = 0.0;

        // 中文：350字/分钟 → 每字 60/350 ≈ 0.171 秒
        seconds += chineseCount * 0.171;

        // 英文：180词/分钟 → 每词 60/180 = 0.333 秒
        seconds += englishWordCount * 0.333;

        // 代码块：阅读速度降为 1/4（非常慢）
        seconds += codeBlockLineCount * 0.171 * 4;  // 相当于中文的 4 倍时间

        // 图片：每张 12 秒（Medium 标准）
        seconds += imageCount * 12;

        // 数学公式：每处 15 秒
        seconds += mathBlockCount * 15;

        // 表格：每行 10 秒（结构复杂）
        seconds += tableRowCount * 10;

        // 列表：每项 3 秒（跳着读也需要时间）
        seconds += listItemCount * 3;

        // 5. 转为分钟，至少 1 分钟，向上取整
        return Math.max(1, (int) Math.ceil(seconds / 60.0));
    }

    /**
     * 统计给定文本中与指定正则模式匹配的次数。
     *
     * @param text 要搜索的文本内容
     * @param pattern 用于匹配的正则表达式模式
     * @return 匹配的次数
     */
    private int countPattern(String text, Pattern pattern) {
        Matcher matcher = pattern.matcher(text);
        int count = 0;
        while (matcher.find()) count++;
        return count;
    }

    /**
     * 统计给定文本中代码块模式匹配的行数总和。
     * 根据代码块的正则表达式模式，计算所有匹配内容的总行数。
     *
     * @param text 要分析的文本内容
     * @return 匹配的代码块总行数
     */
    private int countLinesInPattern(String text) {
        Matcher matcher = PostUtils.CODE_BLOCK_PATTERN.matcher(text);
        int totalLines = 0;
        while (matcher.find()) {
            totalLines += matcher.group().split("\n").length;
        }
        return totalLines;
    }
}

