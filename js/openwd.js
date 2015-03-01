//法律文书上传到服务器操作
function upLoadLegalFiles(bkid)
{	
	//上传正文、定稿正文、盖章正文
	try{
		Initialization(bkid);
		document.forms[0].cal.SetFlag(6,1);	
		var flag=1;
		if  (document.forms[0].cal.GetFlag(1)==1)
		{	//正文
			flag=document.forms[0].cal.SendFile("u0"+bkid,1);
		}
		else if  (document.forms[0].cal.GetFlag(2)==1)
		{	//定稿
	
			flag=document.forms[0].cal.SendFile("u1"+bkid+dbname,2);
			
		}
		else if  (document.forms[0].cal.GetFlag(3)==1)
		{	//定稿正文
	
			flag=document.forms[0].cal.SendFile("u1"+bkid+dbname,3);
			
		}
		else if  (document.forms[0].cal.GetFlag(4)==1)
		{	//盖章
		
			flag=document.forms[0].cal.SendFile("u1"+bkid+dbname,4);
		
		}
		else if  (document.forms[0].cal.GetFlag(5)==1)
		{	//定稿
		
			flag=document.forms[0].cal.SendFile("u0"+bkid+dbname,5);
		}
		else if  (document.forms[0].cal.GetFlag(6)==1)
		{	//上传法律文书
			flag=document.forms[0].cal.SendFile("u1"+bkid,6);
		}
		else if  (document.forms[0].cal.GetFlag(7)==1)
		{	//盖章
			flag=document.forms[0].cal.SendFile("u1"+bkid+dbname,7);
		}
		else if  (document.forms[0].cal.GetFlag(8)==1)
		{	//盖章
			flag=document.forms[0].cal.SendFile("u1"+bkid+dbname,8);
		
		}
		if(flag !=0) return false;
	
		return true;
	}catch(e){
		alert(e);
	}
}
//打开文档
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
//打开文档并修改文档，bookmarks文档中内容插入word标签
function createLegalDoc(doc_url,bookmarks)
{
	Initialization(doc_url)
    //0：编辑  1：修改 2：只读
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
//初始化控件
function Initialization(doc_value){
	var doc_url="";
	var basePath=document.forms[0].path.value+"/";//服务器url
	var ip="";
	var port="";
	if(doc_value.indexOf(".doc")!=-1)
		doc_url=doc_value.substring(0,doc_value.indexOf(".doc"));
	else if(doc_value.indexOf(".docx")!=-1)
		doc_url=doc_value.substring(0,doc_value.indexOf(".docx"));  
	else 
		doc_url=doc_value;
		
	//服务器信息ip+":"+port
	server=document.forms[0].url.value;
	idx=server.indexOf(":");
	if(idx!=-1) 
	{
		ip=server.substring(0,idx);
		port=server.substring(idx+1);}
	else{
	 	alert("未配置服务器ip地址，请与管理员联系！");
		return false;
	}
	var password="80137f633747be19";
	document.forms[0].cal.ServerIp=ip;
	document.forms[0].cal.ServerPort=port;
	document.forms[0].cal.ServerPath=basePath+"openDoc"
   
}
//*****************************************************************************************
