package ru.technoteinfo.site.services.common;

import com.jcraft.jsch.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class SftpService {

    @Value("${ssh.connection.url}")
    private String server;

    @Value("${ssh.connection.port}")
    private int port;

    @Value("${ssh.connection.user}")
    private String username;

    @Value("${ssh.connection.password}")
    private String password;

    @Value("${ssh.web.path}")
    private String path;

    public String uploadFile(String localFile, String remoteFile){
        JSch jsch = new JSch();
        Session session = null;
        try {
            session = jsch.getSession(username, server, port);
            session.setConfig("StrictHostKeyChecking", "no");
            session.setPassword(password);
            session.connect();

            Channel channel = session.openChannel("sftp");
            channel.connect();
            ChannelSftp sftpChannel = (ChannelSftp) channel;
            sftpChannel.put(localFile, path+remoteFile);
            sftpChannel.exit();
            String[] resultPath = remoteFile.split("/");
            List<String> list = new ArrayList<>(Arrays.asList(resultPath));
            if (list.get(0).equals("web")){
                list.remove(0);
            }
            return String.join("/", list);
        } catch (JSchException | SftpException e) {
            e.printStackTrace();
        } finally {
            if (session != null){
                session.disconnect();
            }
        }
        return null;
    }

}
