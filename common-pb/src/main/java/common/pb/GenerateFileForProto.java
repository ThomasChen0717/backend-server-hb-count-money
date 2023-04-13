package common.pb;

import cn.hutool.core.util.ArrayUtil;
import cn.hutool.system.SystemUtil;
import com.iohao.game.widget.light.protobuf.ProtoGenerateFile;

import java.io.File;
import java.util.regex.Matcher;

/**
 * @author mark
 * @date 2023-04-10
 */
public class GenerateFileForProto {
    public static void main(String[] args) {

        /*
         * .proto 文件生成
         * 相关文档 https://www.yuque.com/iohao/game/vpe2t6
         *
         * 运行该类，会在当前项目 target/proto 目录下生成 .proto 文件
         */

        // 需要扫描的包名
        String protoPackagePath = GenerateFileForProto.class.getPackageName();

        String[] protoSourcePathArray = new String[]{
                SystemUtil.getUserInfo().getCurrentDir()
                , "common-pb"
                , "src"
                , "main"
                , "java"
                , protoPackagePath.replaceAll("\\.", Matcher.quoteReplacement(File.separator))
        };

        // 源码目录
        String protoSourcePath = ArrayUtil.join(protoSourcePathArray, File.separator);

        String[] generateFolderArray = new String[]{
                SystemUtil.getUserInfo().getCurrentDir()
                , "common-pb"
                , "target"
                , "proto"
        };

        // 生成 .proto 文件存放的目录
        String generateFolder = ArrayUtil.join(generateFolderArray, File.separator);

        ProtoGenerateFile protoGenerateFile = ProtoGenerateFile.builder()
                // 源码目录
                .protoSourcePath(protoSourcePath)
                // 需要扫描的包名
                .protoPackagePath(protoPackagePath)
                // 生成 .proto 文件存放的目录
                .generateFolder(generateFolder)
                .build();

        // 生成 .proto 文件
        protoGenerateFile.generate();
    }
}
