package com.zok.art.zhihu.inter;

/**
 * @author 赵坤
 * @email artzok@163.com
 */
public interface ICardItem {
   String getDescription();

   void setDescription(String description);

   long getId();

   void setId(long id);

   String getName();

   void setName(String name);

   String getThumbnail();

   void setThumbnail(String thumbnail);
}
