package online.javalab.lib;

import com.blankj.utilcode.util.SPUtils;
import com.blankj.utilcode.util.StringUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.google.googlejavaformat.java.JavaFormatterOptions;

import online.javalab.lib.mode.JavaMaven;

public class JavaStudioSetting {
    public static final String KEY = "JavaSetting";

    public static JavaFormatterOptions getJavaFormatterOptions() {
        String formatter = SPUtils.getInstance(KEY).getString("formatter", "2");
        JavaFormatterOptions.Style style;
        if (StringUtils.equals(formatter, "2")) {
            style = JavaFormatterOptions.Style.GOOGLE;
        } else {
            style = JavaFormatterOptions.Style.AOSP;
        }
        return JavaFormatterOptions.builder()
                .style(style)
                .build();
    }

    public static void restore() {
        SPUtils.getInstance(KEY).put("maven", JavaMaven.MAVEN_JCENTER);
        ToastUtils.showShort("Successful operation");
    }

    public static String getMaven() {
        return SPUtils.getInstance(KEY).getString("maven", JavaMaven.MAVEN_JCENTER);
    }

}
