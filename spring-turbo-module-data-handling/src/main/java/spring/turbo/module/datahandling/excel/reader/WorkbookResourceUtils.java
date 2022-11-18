/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 *    ____             _            _____           _
 *   / ___| _ __  _ __(_)_ __   __ |_   _|   _ _ __| |__   ___
 *   \___ \| '_ \| '__| | '_ \ / _` || || | | | '__| '_ \ / _ \
 *    ___) | |_) | |  | | | | | (_| || || |_| | |  | |_) | (_) |
 *   |____/| .__/|_|  |_|_| |_|\__, ||_| \__,_|_|  |_.__/ \___/
 *         |_|                 |___/   https://github.com/yingzhuo/spring-turbo
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
package spring.turbo.module.datahandling.excel.reader;

import org.apache.poi.hssf.record.crypto.Biff8EncryptionKey;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.crypt.Decryptor;
import org.apache.poi.poifs.crypt.EncryptionInfo;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.core.io.Resource;
import spring.turbo.module.datahandling.excel.ExcelType;

import java.io.IOException;
import java.security.GeneralSecurityException;

/**
 * 打开Excel文件
 *
 * @author 应卓
 * @since 1.0.1
 */
public final class WorkbookResourceUtils {

    private WorkbookResourceUtils() {
        super();
    }

    public static WorkbookAndFileSystem createWorkbook(ExcelType excelType, Resource resource, String password) throws IOException, GeneralSecurityException {
        if (password == null) {
            if (excelType == ExcelType.XSSF) {
                return new WorkbookAndFileSystem(new XSSFWorkbook(resource.getInputStream()));
            } else {
                return new WorkbookAndFileSystem(new HSSFWorkbook(resource.getInputStream()));
            }
        } else {
            POIFSFileSystem fileSystem = new POIFSFileSystem(resource.getInputStream());
            if (excelType == ExcelType.XSSF) {
                EncryptionInfo info = new EncryptionInfo(fileSystem);
                Decryptor decryptor = Decryptor.getInstance(info);
                if (!decryptor.verifyPassword(password)) {
                    throw new IllegalArgumentException("unable to process: wrong password");
                }
                return new WorkbookAndFileSystem(
                        new XSSFWorkbook(decryptor.getDataStream(fileSystem)),
                        fileSystem
                );
            } else {
                Biff8EncryptionKey.setCurrentUserPassword(password);
                return new WorkbookAndFileSystem(new HSSFWorkbook(fileSystem), fileSystem);
            }
        }
    }

}
