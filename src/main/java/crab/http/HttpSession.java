package crab.http;


import com.sun.deploy.net.HttpResponse;
import crab.constant.CrabConst;
import crab.kit.PlatformKit;
import lombok.Getter;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.channels.SocketChannel;
import java.util.Map;

@Getter
public class HttpSession {

    private SocketChannel channel;
    private ByteBuffer buffer = ByteBuffer.allocate(2048);
    private StringBuilder readLines = new StringBuilder();
    private int mark = 0;

    public HttpSession(SocketChannel channel){
        this.channel = channel;
    }

    public String line(){
        return readLines.toString();
    }

    /**
     * Try to read a line
     * @return
     * @throws IOException
     */
    public String read() throws IOException{
        StringBuilder sb = new StringBuilder();
        int l = -1;
        while (buffer.hasRemaining()){
            char c = (char) buffer.get();
            sb.append(c);
            if (PlatformKit.isWindow()) {
                //it is designed to remove separators on windows
                if (c == '\n' && l == '\r') {
                    //mark position
                    mark = buffer.position();
                    // add to the total line
                    readLines.append(sb);
                    // return with no line separators
                    return sb.substring(0, sb.length() - 2);
                }
                l = c;

            }else if (PlatformKit.isLinux() || PlatformKit.isMac()){
                if (c == '\n'){
                    mark = buffer.position();
                    readLines.append(sb);
                    return sb.substring(0,sb.length() - 1);
                }
            }
        }

        return null;

    }


    /**
     * Get more data from the stream
     * @throws IOException
     */
    public void readData() throws IOException{
        buffer.limit(buffer.capacity());
        int read = channel.read(buffer);
        if (read != -1){
            buffer.flip();
            buffer.position(mark);
        }
    }

    public void writeLine(String line) throws IOException{
        channel.write(CrabConst.ENCODER.encode(CharBuffer.wrap(line + "\r\n")));
    }

    public void sendResponse(crab.http.HttpResponse response){
        response.addDefaultHeaders();
        try{
            writeLine(response.getVersion()+" "+response.getStatusCode()+" "+ response.getReason());
            for (Map.Entry<String,String> header : response.getHeaders().entrySet()){
                writeLine(header.getKey()+": "+header.getValue());
            }
            writeLine("");

            byte[] content = response.bytes();

            getChannel().write(ByteBuffer.wrap(content));
        }catch (IOException e){
            e.printStackTrace();
        }

    }

    public void close(){
        try {
            channel.close();
        }catch (IOException e){
            e.printStackTrace();
        }
    }


}