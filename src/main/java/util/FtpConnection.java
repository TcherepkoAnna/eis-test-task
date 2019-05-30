package util;

import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPReply;
import org.apache.log4j.Logger;

import java.io.IOException;

public class FtpConnection {

    private final static Logger LOG = Logger.getLogger(FtpConnection.class);
    FTPClient client;
    String host;

    public FtpConnection(String host) throws IOException {
        this.client = new FTPClient();
        this.host = host;
    }


    public void connect() throws IOException {
        LOG.debug("setting connection");
        client.connect(host);
        int replyCode = client.getReplyCode();
        if (!FTPReply.isPositiveCompletion(replyCode)) {
            LOG.error("Operation failed. Server reply code: " + replyCode);
            return;
        }
    }

    public void login(String name, String password) throws IOException {
        boolean success = client.login(name, password);
        if (success) {
            LOG.info("Connection established...");
        } else {
            LOG.info("Connection fail...");
        }
    }

    public  void disconnectFromServer() throws IOException {
        LOG.debug("logging out and disconnecting from server");
        if (client.isConnected()) {
            client.logout();
            client.disconnect();
        }
    }

    public FTPFile[] getDirectoriesList() throws IOException {
        LOG.debug("getting directories");
        return client.listDirectories();
    }

    public  void deleteDirectory(String dirToCreate) throws IOException {
        LOG.debug("deleting directory");
        boolean deleted = client.removeDirectory(dirToCreate);
        if (deleted) {
            LOG.info("The directory was removed successfully.");
        } else {
            LOG.info("Could not delete the directory, it may not be empty");
        }
    }

    public  void createDirectory(String dirToCreate) throws IOException {
        LOG.debug("creating directory");
        boolean success = client.makeDirectory(dirToCreate);
        showServerReply();
        if (success) {
            LOG.info("Successfully created directory: " + dirToCreate);
        } else {
            LOG.info("Failed to create directory. See server's reply.");
        }
    }

    public  void changeDirectory(String name) throws IOException {
        boolean success = client.changeWorkingDirectory("/" + name);
        showServerReply();
        if (success) {
            LOG.info("Successfully changed working directory.");
        } else {
            LOG.info("Failed to change working directory. See server's reply.");
        }
    }

    private  void showServerReply() {
        LOG.debug("getting reply from server");
        String[] replies = client.getReplyStrings();
        if (replies != null && replies.length > 0) {
            for (String aReply : replies) {
                LOG.info("SERVER: " + aReply);
            }
        }
    }
}
