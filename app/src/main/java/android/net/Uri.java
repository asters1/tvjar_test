package android.net;

import java.net.URL;

public class Uri {
  //主机名
  private String host;
  //协议
  private String protocol;
  //端口
  private int port;
  //路径
  private String path;
  //查询字符串
  private String query;
  //片段标识符
  private String fragment;



  public static Uri  parse(String url){
    try {
      Uri uri1=new Uri();
      URL u1=new URL(url);


      uri1.host=u1.getHost();
      uri1.protocol=u1.getProtocol();
      uri1.port=u1.getPort();
      uri1.path=u1.getPath();
      uri1.query=u1.getQuery();
      uri1.fragment=u1.getRef();

      return uri1;
    } catch (Exception e) {
      return null;
    }


  }
  //返回协议，例如http，https
  public String getScheme(){
    return this.protocol;
  }
  //返回主机名
  public String getHost(){
    return this.host;
  }

}
