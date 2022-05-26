package online.javalab.lib.mode;

import androidx.annotation.NonNull;

import com.blankj.utilcode.util.ConvertUtils;
import com.blankj.utilcode.util.LogUtils;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.FileCallback;
import com.lzy.okgo.model.Response;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import online.javalab.lib.JavaStudioSetting;
import online.javalab.lib.bean.Dependency;

public class JavaMaven {
    private static JavaMaven javaMaven;
    private static DocumentBuilder builder;

    public static final String MAVEN_JCENTER = "https://jcenter.bintray.com";

    JavaMaven() {
        try {
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            builder = dbFactory.newDocumentBuilder();
        } catch (Exception e) {
            LogUtils.e(e.toString());
        }
    }

    public static JavaMaven getInstance() {
        if (javaMaven == null) {
            synchronized (JavaMaven.class) {
                if (javaMaven == null) {
                    javaMaven = new JavaMaven();
                }
            }
        }
        return javaMaven;
    }

    /**
     * Resolve dependencies
     *
     * @param dependXml Depends on xml
     * @return rely
     */
    public List<Dependency> getDependence(String dependXml) {
        try {
            InputStream inputStream = ConvertUtils.string2InputStream(dependXml, "utf-8");
            Document document = builder.parse(inputStream);
            List<Dependency> dependencies = new ArrayList<>();
            NodeList elements = document.getElementsByTagName("dependency");
            for (int i = 0; i < elements.getLength(); i++) {
                Element element = (Element) elements.item(i);
                String groupId = element.getElementsByTagName("groupId").item(0).getTextContent();
                String artifactId = element.getElementsByTagName("artifactId").item(0).getTextContent();
                String version = element.getElementsByTagName("version").item(0).getTextContent();
                Dependency dependency = new Dependency(groupId, artifactId, version);
                dependencies.add(dependency);
            }
            return dependencies;
        } catch (Exception e) {
            LogUtils.e(e.toString());
        }
        return new ArrayList<>();
    }

    /**
     * Download dependency
     *
     * @param dependency       rely
     * @param downloadDir      Save folder
     * @param downloadListener Download listener
     */
    public void downloadDependence(@NonNull Dependency dependency, String downloadDir, MavenDownloadListener downloadListener) {
        List<Dependency> dependencies = new ArrayList<>();
        dependencies.add(dependency);
        downloadDependence(dependencies, downloadDir, downloadListener);
    }

    /**
     * Download dependency
     *
     * @param dependencies     rely
     * @param downloadDir      Save folder
     * @param downloadListener Download listener
     */
    public void downloadDependence(@NonNull List<Dependency> dependencies, String downloadDir, MavenDownloadListener downloadListener) {
        for (int i = 0; i < dependencies.size(); i++) {
            // getting information
            Dependency dependency = dependencies.get(i);
            String groupId = dependency.getGroupId();
            String artifactId = dependency.getArtifactId();
            String version = dependency.getVersion();
            // Replacement symbol
            groupId = groupId.replace(".", "/");
            artifactId = artifactId.replace(".", "/");
            // Obtain dependent libraries
            String dependenceMaven = JavaStudioSetting.getMaven();
            // Splicing
            String dependenceFilePath = "/" + groupId + "/" + artifactId + "/" + version + "/" + artifactId + "-" + version + ".jar";
            String dependenceUrl = dependenceMaven + dependenceFilePath;
            String dependenceName = artifactId + "-" + version + ".jar";

            // Download dependency
            OkGo.<File>get(dependenceUrl)
                    .execute(new FileCallback(downloadDir, dependenceName) {
                        @Override
                        public void onSuccess(Response<File> response) {
                            downloadListener.onFile(response.body());
                        }

                        @Override
                        public void onError(Response<File> response) {
                            downloadListener.onError(response.getException().toString());
                        }
                    });
        }
    }

    /**
     * Download listener
     */
    public interface MavenDownloadListener {
        void onFile(File body);

        void onError(String failMsg);
    }
}
