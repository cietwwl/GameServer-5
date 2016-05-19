
/**
 * 翻页跳转
 * @param pageIndex
 * @return
 */
function jumpPage(pageIndex){
	var curPage = document.getElementById('curPage');	
	curPage.value=pageIndex;	
	document.forms[0].submit();
}


function obj2str(o){
    var r = [];
    if(typeof o =="string") return "\""+o.replace(/([\'\"\\])/g,"\\$1").replace(/(\n)/g,"\\n").replace(/(\r)/g,"\\r").replace(/(\t)/g,"\\t")+"\"";
    if(typeof o =="undefined") return "undefined";
    if(typeof o == "object"){
        if(o===null) return "null";
        else if(!o.sort){
            for(var i in o)
                r.push("\""+i+"\":"+obj2str(o[i]))
            r="{"+r.join()+"}"
        }else{
            for(var i =0;i<o.length;i++)
                r.push(obj2str(o[i]))
            r="["+r.join()+"]"
        }
        return r;
    }
    return o.toString();
}

function randomInt( min, max){
	return parseInt(Math.random()*(max-min)+min);
}

String.prototype.startWith=function(str){     
  var reg=new RegExp("^"+str);     
  return reg.test(this);        
} 
 
String.prototype.endWith=function(str){     
  var reg=new RegExp(str+"$");     
  return reg.test(this);        
}