#define intPara(k,v)
  #if(v && v!=0) AND #(k) = #para(v) #end
#end


#define page()
  #if(offset) LIMIT #para(offset),#para(limit)
  #elseif(limit) LIMIT #para(limit)
  #end
#end

###列表参数
#define ids(ids)
  #for(id:ids) #para(id) #(for.last?'':',') #end
#end


#define idsPara(k,ids)
  #if(ids)
    AND #(k) #(isNot?'NOT':'') IN (
      #@ids(ids)
    )
  #end
#end


### map参数
#define mapPara(map)
  #for(x : map)
    #if(x.value) AND #(x.key) = #para(x.value) #end
  #end
#end

#define notMapPara(map)
  #for(x : map)
    #if(x.value) AND (#(x.key) IS NULL OR #(x.key) != #para(x.value)) #end
  #end
#end

#define simpleSelect(tableName, withStatus)
  SELECT #(field??'*') FROM #(tableName) WHERE 1=1
  #if(withStatus) AND (status != 'delete' OR status != 0) #end
  #@mapPara(para)
  #@notMapPara(notPara)
#end

#define simpleCount(tableName, withStatus)
  SELECT COUNT(*) FROM #(tableName) WHERE 1=1
  #if(withStatus) AND (status != 'delete' OR status != 0) #end
  #@mapPara(para)
  #@notMapPara(notPara)
#end