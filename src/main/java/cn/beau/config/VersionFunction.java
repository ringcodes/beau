package cn.beau.config;

import org.beetl.core.Context;
import org.beetl.core.Function;
import org.springframework.core.io.ClassPathResource;
import org.springframework.util.DigestUtils;
import org.springframework.util.FileCopyUtils;
import org.springframework.util.StringUtils;

import java.util.Map;
import java.util.StringJoiner;
import java.util.concurrent.ConcurrentHashMap;

public class VersionFunction implements Function {
    private Map<String, String> versionMap = new ConcurrentHashMap<>();
    private final static String DELIMITER = "?";

    @Override
    public String call(Object[] paras, Context ctx) {
        String path = String.valueOf(paras[0]);
        try {
            String pathVersion = versionMap.get(path);
            if (!StringUtils.hasText(pathVersion)) {
                pathVersion = md5(path);
                versionMap.put(path, pathVersion);
            }
            StringJoiner sj = new StringJoiner(DELIMITER);
            sj.add(path);
            sj.add(pathVersion);
            return sj.toString();
        } catch (Exception e) {
            return null;
        }
    }

    private String md5(String path) {
        try {
            ClassPathResource cpr = new ClassPathResource(path);
            byte[] content = FileCopyUtils.copyToByteArray(cpr.getInputStream());
            return DigestUtils.md5DigestAsHex(content);
        } catch (Exception e) {
            return null;
        }
    }
}
