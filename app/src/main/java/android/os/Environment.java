package android.os;

import java.io.File;

public  class Environment {
  public static File getExternalStorageDirectory(){
    File StoragePath = new File("/storage/emulated/0");
    return StoragePath;
  }
  
}
