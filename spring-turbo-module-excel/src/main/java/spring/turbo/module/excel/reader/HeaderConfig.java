/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 *    ____             _            _____           _
 *   / ___| _ __  _ __(_)_ __   __ |_   _|   _ _ __| |__   ___
 *   \___ \| '_ \| '__| | '_ \ / _` || || | | | '__| '_ \ / _ \
 *    ___) | |_) | |  | | | | | (_| || || |_| | |  | |_) | (_) |
 *   |____/| .__/|_|  |_|_| |_|\__, ||_| \__,_|_|  |_.__/ \___/
 *         |_|                 |___/   https://github.com/yingzhuo/spring-turbo
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
package spring.turbo.module.excel.reader;

import spring.turbo.bean.Pair;
import spring.turbo.bean.Tuple;
import spring.turbo.lang.Mutable;
import spring.turbo.util.Asserts;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @author 应卓
 * @since 1.0.0
 */
@Mutable
public final class HeaderConfig implements Serializable {

    private final List<Pair<String, Integer>> sheetNameConfig = new ArrayList<>();
    private final List<Pair<Integer, Integer>> sheetIndexConfig = new ArrayList<>();
    private final List<Tuple<String, Integer, String[]>> sheetNameFixedHeader = new ArrayList<>();
    private final List<Tuple<Integer, Integer, String[]>> sheetIndexFixedHeader = new ArrayList<>();

    private HeaderConfig() {
        super();
    }

    public static HeaderConfig newInstance() {
        return new HeaderConfig();
    }

    public HeaderConfig bySheetName(String sheetName, int rowIndex) {
        Asserts.hasText(sheetName);
        Asserts.isTrue(rowIndex >= 0);
        sheetNameConfig.add(Pair.of(sheetName, rowIndex));
        return this;
    }

    public HeaderConfig bySheetIndex(int sheetIndex, int rowIndex) {
        Asserts.isTrue(sheetIndex >= 0);
        Asserts.isTrue(rowIndex >= 0);
        sheetIndexConfig.add(Pair.of(sheetIndex, rowIndex));
        return this;
    }

    public HeaderConfig fixed(int sheetIndex, String... header) {
        return fixed(sheetIndex, 0, header);
    }

    public HeaderConfig fixed(int sheetIndex, int firstCellIndex, String... header) {
        sheetIndexFixedHeader.add(Tuple.of(sheetIndex, firstCellIndex, header));
        return this;
    }

    public HeaderConfig fixed(String sheetName, String... header) {
        return fixed(sheetName, 0, header);
    }

    public HeaderConfig fixed(String sheetName, int firstCellIndex, String... header) {
        sheetNameFixedHeader.add(Tuple.of(sheetName, firstCellIndex, header));
        return this;
    }

    public List<Pair<String, Integer>> getSheetNameConfig() {
        return sheetNameConfig;
    }

    public List<Pair<Integer, Integer>> getSheetIndexConfig() {
        return sheetIndexConfig;
    }

    public List<Tuple<String, Integer, String[]>> getSheetNameFixedHeader() {
        return sheetNameFixedHeader;
    }

    public List<Tuple<Integer, Integer, String[]>> getSheetIndexFixedHeader() {
        return sheetIndexFixedHeader;
    }
}
