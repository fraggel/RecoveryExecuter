package com.fraggel.recoveryexecuter.pro;
import java.io.File;
import java.util.ArrayList;
import java.util.Scanner;

import android.os.Environment;

public class StorageOptions {
	private static ArrayList<String> mMounts = new ArrayList<String>();
	public static ArrayList<String> mMountsF = new ArrayList<String>();
	private static ArrayList<String> mVold = new ArrayList<String>();
	private static ArrayList<String> listaVold=new ArrayList<String>();
	public static ArrayList<String> listaVoldF=new ArrayList<String>();

	public static String[] labels;
	public static String[] paths;
	public static int count = 0;

	public static void determineStorageOptions() {
		readMountsFile();
		readVoldFile();
		compareMountsWithVold();
		testAndCleanMountsList();
		//setProperties();
	}
	public static void determineStorageOptionsMount(String sdCard,String extSdCard) {
			readMountsFileMount(sdCard,extSdCard);
			/*readVoldFile();
			compareMountsWithVold();
			testAndCleanMountsList();*/
			//setProperties();
	}
	private static void readMountsFile() {
		/*
		 * Scan the /proc/mounts file and look for lines like this:
		 * /dev/block/vold/179:1 /mnt/sdcard vfat rw,dirsync,nosuid,nodev,noexec,relatime,uid=1000,gid=1015,fmask=0602,dmask=0602,allow_utime=0020,codepage=cp437,iocharset=iso8859-1,shortname=mixed,utf8,errors=remount-ro 0 0
		 * 
		 * When one is found, split it into its elements
		 * and then pull out the path to the that mount point
		 * and add it to the arraylist
		 */

		try {
			Scanner scanner = new Scanner(new File("/proc/mounts"));
			while (scanner.hasNext()) {
				String line = scanner.nextLine();
				
				if (line.startsWith("/dev/block/vold/")) {
					String[] lineElements = line.split(" ");
					lineElements[1].replaceAll(":.*$", "");
					mMounts.add(lineElements[1]);
				}
			}
		} catch (Exception e) {
			// Auto-generated catch block
			e.printStackTrace();
		}
	}
	private static void readMountsFileMount(String sdcard,String extSdCard) {
		/*
		 * Scan the /proc/mounts file and look for lines like this:
		 * /dev/block/vold/179:1 /mnt/sdcard vfat rw,dirsync,nosuid,nodev,noexec,relatime,uid=1000,gid=1015,fmask=0602,dmask=0602,allow_utime=0020,codepage=cp437,iocharset=iso8859-1,shortname=mixed,utf8,errors=remount-ro 0 0
		 * 
		 * When one is found, split it into its elements
		 * and then pull out the path to the that mount point
		 * and add it to the arraylist
		 */

		try {
			Scanner scanner = new Scanner(new File("/proc/mounts"));
			String dataTMP=null;
			while (scanner.hasNext()) {
				String line = scanner.nextLine();
				if(line.indexOf(sdcard)!=-1 || line.indexOf(extSdCard)!=-1){
					String tmp=line.substring(0,line.indexOf(" "));
					if(line.startsWith("/dev/block/vold/")){
						String[] tnp=line.split("/");
						String tt=tnp[4];
						String[] numbers=tt.split(":");
						Scanner scan=new Scanner(new File("/proc/partitions"));
						while (scan.hasNext()) {
							String line2 = scan.nextLine();
							//Line2 tiene la ruta entera pero hay que separar por los numbers[0] y numbers[1] y una vez con esa linea
							// hacemos un split por " " y cogemos el último
							if(line2.indexOf(String.valueOf(numbers[0])+"       "+String.valueOf(numbers[1]))!=-1){
								String[] split = line2.split(" ");
								for (int i = 0; i < split.length; i++) {
									String string = split[i].trim();
									if(!"".equals(string) && !numbers[0].trim().equals(string) && !numbers[1].trim().equals(string)){
										try {
											Integer.valueOf(string);
										} catch (Exception e) {
											String ttt=string;
											listaVoldF.add("/dev/block/"+ttt);
										}
										
									}
								}
							}
						}
					}else{
						tmp=line;
						if(line.indexOf("/data ")!=-1){
							tmp=line.substring(0,line.indexOf("/data "));
							dataTMP=new String();
							dataTMP=tmp;
							listaVoldF.add(tmp);
						}
					}
					
					//listaVold.add(line);
				}else{
					String tmp=line;
					if(line.indexOf("/data ")!=-1){
						tmp=line.substring(0,line.indexOf("/data "));
						dataTMP=new String();
						dataTMP=tmp;
						listaVoldF.add(tmp);
					}
				}
				if (line.startsWith("/dev/block/vold/")) {
					String[] lineElements = line.split(" ");
					lineElements[1].replaceAll(":.*$", "");
					mMountsF.add(lineElements[1]);
				}/*else if(line.lastIndexOf("fuse")!=-1){
					String[] lineElements = line.split(" ");
					lineElements[1].replaceAll(":.*$", "");
					mMounts.add(lineElements[1]);				
				}*/else if(dataTMP!=null && line.lastIndexOf(dataTMP)!=-1){
					String[] lineElements = line.split(" ");
					lineElements[1].replaceAll(":.*$", "");
					mMountsF.add(lineElements[1]);				
				}
			}
		} catch (Exception e) {
			// Auto-generated catch block
			e.printStackTrace();
		}
	}

	private static void readVoldFile() {
		/*
		 * Scan the /system/etc/vold.fstab file and look for lines like this:
		 * dev_mount sdcard /mnt/sdcard 1 /devices/platform/s3c-sdhci.0/mmc_host/mmc0
		 * 
		 * When one is found, split it into its elements
		 * and then pull out the path to the that mount point
		 * and add it to the arraylist
		 */

		try {
			Scanner scanner = new Scanner(new File("/system/etc/vold.fstab"));
			while (scanner.hasNext()) {
				String line = scanner.nextLine();
				if (line.startsWith("dev_mount")) {
					String[] lineElements = line.split(" "); 
					lineElements[2] = lineElements[2].replaceAll(":.*$", "");
					mVold.add(lineElements[2]);
				}
			}
		} catch (Exception e) {
			// Auto-generated catch block
			e.printStackTrace();
		}
	}

	private static void compareMountsWithVold() {
		/*
		 * Sometimes the two lists of mount points will be different.
		 * We only want those mount points that are in both list.
		 * 
		 * Compare the two lists together and remove items that are not in both lists.
		 */

		for (int i = 0; i < mMounts.size(); i++) {
			String mount = mMounts.get(i);
			if (!mVold.contains(mount)) 
				mMounts.remove(i--);
		}

		// don't need this anymore, clear the vold list to reduce memory
		// use and to prepare it for the next time it's needed.
		mVold.clear();
	}

	private static void testAndCleanMountsList() {
		/*
		 * Now that we have a cleaned list of mount paths
		 * Test each one to make sure it's a valid and
		 * available path. If it is not, remove it from
		 * the list. 
		 */

		for (int i = 0; i < mMounts.size(); i++) {
			String mount = mMounts.get(i);
			File root = new File(mount);
			if (!root.exists() || !root.isDirectory() || !root.canWrite())
				mMounts.remove(i--);
		}
	}

	private static void setProperties() {
		/*
		 * At this point all the paths in the list should be
		 * valid. Build the public properties.
		 */

		ArrayList<String> mLabels = new ArrayList<String>();

		int i = 1;
		for (String path: mMounts)
		{
			if ("/mnt/sdcard".equalsIgnoreCase(path) || "/storage/sdcard0".equalsIgnoreCase(path) || Environment.getExternalStorageDirectory().getPath().equalsIgnoreCase(path))
				mLabels.add("Built-in Storage");
			else
				mLabels.add("External SD Card " + i++);
		}
		
		labels = new String[mLabels.size()];
		mLabels.toArray(labels);

		paths = new String[mMounts.size()];
		mMounts.toArray(paths);

		count = Math.min(labels.length, paths.length);

		// don't need this anymore, clear the mounts list to reduce memory
		// use and to prepare it for the next time it's needed.
		mMounts.clear();
	}
	public static ArrayList<String> getMounts(){
		return mMounts;
	}
	
}