//���������ϴ�������������
function upLoadLegalFiles(bkid)
{	
	//�ϴ����ġ��������ġ���������
	try{
		Initialization(bkid);
		document.forms[0].cal.SetFlag(6,1);	
		var flag=1;
		if  (document.forms[0].cal.GetFlag(1)==1)
		{	//����
			flag=document.forms[0].cal.SendFile("u0"+bkid,1);
		}
		else if  (document.forms[0].cal.GetFlag(2)==1)
		{	//����
	
			flag=document.forms[0].cal.SendFile("u1"+bkid+dbname,2);
			
		}
		else if  (document.forms[0].cal.GetFlag(3)==1)
		{	//��������
	
			flag=document.forms[0].cal.SendFile("u1"+bkid+dbname,3);
			
		}
		else if  (document.forms[0].cal.GetFlag(4)==1)
		{	//����
		
			flag=document.forms[0].cal.SendFile("u1"+bkid+dbname,4);
		
		}
		else if  (document.forms[0].cal.GetFlag(5)==1)
		{	//����
		
			flag=document.forms[0].cal.SendFile("u0"+bkid+dbname,5);
		}
		else if  (document.forms[0].cal.GetFlag(6)==1)
		{	//�ϴ���������
			flag=document.forms[0].cal.SendFile("u1"+bkid,6);
		}
		else if  (document.forms[0].cal.GetFlag(7)==1)
		{	//����
			flag=document.forms[0].cal.SendFile("u1"+bkid+dbname,7);
		}
		else if  (document.forms[0].cal.GetFlag(8)==1)
		{	//����
			flag=document.forms[0].cal.SendFile("u1"+bkid+dbname,8);
		
		}
		if(flag !=0) return false;
	
		return true;
	}catch(e){
		alert(e);
	}
}
//���ĵ�
function viewLegalDoc(doc_url,power)
{
	Initialization(doc_url);
	
	document.forms[0].cal.GetDocumentFile("d1z9999999999999999999999999","zhangletao",1,0);
	 
	//if(flag==0) {
		//document.forms[0].cal.SetFlag(6,1);
		//wintitle=document.title+" - Microsoft Internet Explorer";
		//document.forms[0].cal.ShowWindows(wintitle,-1);
	//}
}
//���ĵ����޸��ĵ���bookmarks�ĵ������ݲ���word��ǩ
function createLegalDoc(doc_url,bookmarks)
{
	Initialization(doc_url)
    //0���༭  1���޸� 2��ֻ��
	power=1;
	var flag=document.forms[0].cal.FinalFax("d"+power+"z"+doc_url,bookmarks);
	if(flag==0) 
	{
		document.forms[0].cal.SetFlag(6,1);	
		wintitle=document.title+" - Microsoft Internet Explorer";
		document.forms[0].cal.ShowWindows(wintitle,-1);
		return true;	
	}else
		return false;
}
//��ʼ���ؼ�
function Initialization(doc_value){
	var doc_url="";
	var basePath=document.forms[0].path.value+"/";//������url
	var ip="";
	var port="";
	if(doc_value.indexOf(".doc")!=-1)
		doc_url=doc_value.substring(0,doc_value.indexOf(".doc"));
	else if(doc_value.indexOf(".docx")!=-1)
		doc_url=doc_value.substring(0,doc_value.indexOf(".docx"));  
	else 
		doc_url=doc_value;
		
	//��������Ϣip+":"+port
	server=document.forms[0].url.value;
	idx=server.indexOf(":");
	if(idx!=-1) 
	{
		ip=server.substring(0,idx);
		port=server.substring(idx+1);}
	else{
	 	alert("δ���÷�����ip��ַ���������Ա��ϵ��");
		return false;
	}
	var password="80137f633747be19";
	document.forms[0].cal.ServerIp=ip;
	document.forms[0].cal.ServerPort=port;
	document.forms[0].cal.ServerPath=basePath+"openDoc"
   
}
//*****************************************************************************************
