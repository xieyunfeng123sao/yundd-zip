package com.code;
public class Constants 
{
	public static final int success = 0;
	public static final int fail = 1;
	
	//������
	public interface VeyeDevDefinitionType{
		int VeyeDevDefinitionType_SD = 0;  //����
		int VeyeDevDefinitionType_HD = 1; //����
	}
	
	public interface DefinitionTypePam{
		int DefinitionTypePam_SD_Width = 480;  
		int DefinitionTypePam_SD_Hight = 640; 
		int DefinitionTypePam_SD_FrameRate = 10;
		
		int DefinitionTypePam_HD_Width = 720;  
		int DefinitionTypePam_HD_Hight = 1280; 
		int DefinitionTypePam_HD_FrameRate = 6;
	}
}
