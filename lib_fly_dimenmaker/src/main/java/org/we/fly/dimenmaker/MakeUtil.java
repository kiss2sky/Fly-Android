package org.we.fly.dimenmaker;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigDecimal;

/**
 * @author: Albert Li
 * @contact: albertlii@163.com
 * @time: 2020/6/23 12:06 PM
 * @description: 本地生成values适配文件的工具类
 * @since: 1.0.0
 */
public class MakeUtil {
    private static final String XML_HEADER = "<?xml version=\"1.0\" encoding=\"utf-8\"?>\r\n";
    private static final String XML_RESOURCE_START = "<resources>\r\n";
    private static final String XML_RESOURCE_END = "</resources>\r\n";
    private static final String XML_DIMEN_TEMPLETE = "<dimen name=\"px_%2$d\">%3$.2fdp</dimen>\r\n";

    private static final int MAX_SIZE = 2 * DimenMaker.SMALLEST_WIDTH;

    /**
     * 生成的文件名
     */
    private static final String XML_NAME = "dimens.xml";

    public static float px2dip(float pxValue, int sw, int designWidth) {
        float dpValue = (pxValue / (float) designWidth) * sw;
        BigDecimal bigDecimal = new BigDecimal(dpValue);
        float finDp = bigDecimal.setScale(2, BigDecimal.ROUND_HALF_UP).floatValue();
        return finDp;
    }

    /**
     * 生成所有的尺寸数据
     *
     * @param type
     * @return
     */
    private static String makeAllDimens(DimenType type, int designWidth) {
        float dpValue;
        String temp;
        StringBuilder sb = new StringBuilder();
        try {
            sb.append(XML_HEADER);
            sb.append(XML_RESOURCE_START);
            for (int i = 0; i <= MAX_SIZE; i++) {
                dpValue = px2dip((float) i, type.getSwWidthDp(), designWidth);
                temp = String.format(XML_DIMEN_TEMPLETE, "", i, dpValue);
                sb.append(temp);
            }
            sb.append(XML_RESOURCE_END);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return sb.toString();
    }


    /**
     * 生成的目标文件夹
     * 只需传宽进来就行
     *
     * @param type     枚举类型
     * @param buildDir 生成的目标文件夹
     */
    public static void makeAll(int designWidth, DimenType type, String buildDir) {
        try {
            // 生成规则
            final String folderName;
            if (type.getSwWidthDp() > 0) {
                // 适配Android 3.2+
                folderName = "values-sw" + type.getSwWidthDp() + "dp";
            } else {
                return;
            }

            // 生成目标目录
            File file = new File(buildDir + File.separator + folderName);
            if (!file.exists()) {
                file.mkdirs();
            }
            // 生成values文件
            FileOutputStream fos = new FileOutputStream(file.getAbsolutePath() + File.separator + XML_NAME);
            fos.write(makeAllDimens(type, designWidth).getBytes());
            fos.flush();
            fos.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
