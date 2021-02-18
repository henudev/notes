public void addTemplate(TemplateAddBean templateAddBean, String userId) throws Exception {
       templateAddBean.setTemplateNum(templateAddBean.getTemplateNum().toLowerCase());
       List<String> tableList = new ArrayList<>();

       if (templateAddBean == null) {
           throw new BusinessException(PublicResultConstant.PARAM_ERROR);
       }
       if ("".equals(templateAddBean.getTemplateNum())) {
           throw new BusinessException("父" + PublicResultConstant.TEMPLATE_NUM_IS_NULL);
       }
       if (templateAddBean.getDepartmentId().size() == 0) {
           throw new BusinessException("父模板部门不能为空！");
       }

       if (templateAddBean.getItemBeans().size() == 0) {
           throw new BusinessException("父模板行属性不能为空！");
       }
       try {
           checkAndCreateTable(templateAddBean, "");
           tableList.add(templateAddBean.getTemplateSourceName() + "." + templateAddBean.getTemplateNum());
           //插入模板信息
           getIdAndCreateTable(templateAddBean, userId);
       } catch (Exception e) {
           logger.info(e.getMessage());
           for (String tableName : tableList) {
               createTableBeanMapper.deleteTable(tableName);
           }
           throw new BusinessException(e.getMessage());
       }
   }



   public static Map<String, String> createTableMap(TemplateAddBean templateAddBean) {
           try {
               List<String> fieldList = new ArrayList<>();
               List<TemplateItemBean> templateItemBeanList = templateAddBean.getItemBeans();
               Map<String, String> map = new HashMap<>();
               String sql = "( id varchar(64)  NOT NULL ,";
               for (int i = 0; i < templateItemBeanList.size(); i++) {
                   String field = templateItemBeanList.get(i).getItemName();
                   //校验表格中是否含有id及重复的标识
                   if ("id".equals(field.toUpperCase())) {
                       throw new BusinessException("表格中的标识不能用id或ID或iD或Id！");
                   }
                   for (String str : fieldList) {
                       if (field.equals(str)) {
                           throw new BusinessException("表格中的名称或标识有重复！");
                       }
                   }
                   fieldList.add(field);
                   String type = setType(templateItemBeanList.get(i).getType());
                   sql += field + " " + type + " " + "comment '" + templateItemBeanList.get(i).getItemDesc() + "',";
               }
               sql = sql + "update_user varchar(150) comment '数据上传用户ID', " +
                       "update_time timestamp NULL default CURRENT_TIMESTAMP comment '数据上传时间'," +
                       "num_id bigint(20) PRIMARY KEY NOT NULL AUTO_INCREMENT comment '数据排序ID') comment '"
                       + templateAddBean.getTemplateName() + "'";
               map.put("name", templateAddBean.getTemplateSourceName() + "." + templateAddBean.getTemplateNum());
               map.put("fields", sql);
               return map;
           } catch (Exception e) {
               e.printStackTrace();
           }
           return null;
       }


   /**
    * 校验并创建表
    *
    * @param templateAddBean
    * @param flag
    * @throws BusinessException
    */
   private void checkAndCreateTable(TemplateAddBean templateAddBean, String flag) throws Exception, BusinessException {
       TemplateEntity tmp = templateEntityMapperExt.selectByTemplateId(templateAddBean.getTemplateSourceName(),
               templateAddBean.getTemplateNum());
       TemplateEntity tmp2 = templateEntityMapperExt.selectByTemplateName(templateAddBean.getTemplateName());
       //
       if (tmp != null) {
           throw new BusinessException(flag + PublicResultConstant.TEMPLATE_NUM_REPETITION);
       }
       if (tmp2 != null) {
           throw new BusinessException(flag + PublicResultConstant.TEMPLATE_NAME_REPETITION);
       }
       try {
           Map<String, String> map = createTableMap(templateAddBean);
           String name = map.get("name");
           String fields = map.get("fields");
           createTableBeanMapper.createTable(name, fields);
       } catch (Exception e) {
           e.printStackTrace();
           if (e instanceof  BadSqlGrammarException){
               throw new BusinessException(templateAddBean.getTemplateNum() + "字符串字段个数超过84，请将部分字段设置为长文本类型！");
           }else {
               throw new BusinessException(templateAddBean.getTemplateNum() + "建表失败！");
           }

       }
   }

   /**
    * 插入模板信息并返回模板id
    *
    * @param templateAddBean
    * @return
    * @throws Exception
    */
   private Integer getIdAndCreateTable(TemplateAddBean templateAddBean, String userId) throws Exception {

       String number = templateAddBean.getTemplateNum();
       String templateSourceName = templateAddBean.getTemplateSourceName();
       templateEntityMapperExt.insertTemplate(number, templateAddBean.getTemplateName(),
               templateAddBean.getDescription(), templateAddBean.getTempTagId(), userId,
               new Date(), templateAddBean.getFillInPeriodKey(), templateSourceName, "0");
       //获取模板id
       TemplateEntity templateEntity = templateEntityMapperExt.selectByTemplateId(templateSourceName, number);
       Integer templateId = templateEntity.getId();

       //新增模板和部门关系
       List<AuthDepartmentInfoEntity> dptList = templateAddBean.getDepartmentId();
       for (AuthDepartmentInfoEntity dpt : dptList) {
           tempToDptService.insert(templateId, dpt.getId());
       }
       List<TemplateItemBean> itemBeanList = templateAddBean.getItemBeans();
       for (TemplateItemBean templateItemBean : itemBeanList) {
           if ("".equals(templateItemBean.getItemName())) {
               throw new BusinessException(PublicResultConstant.TEMPLATE_NUM_IS_NULL);
           }
           if ("".equals(templateAddBean.getTemplateNum())) {
               throw new BusinessException(PublicResultConstant.TEMPLATE_NUM_IS_NULL);
           }
           TableDescriptionEntity tableDescriptionEntityTmp = new TableDescriptionEntity();
           if (templateItemBean.getType() == Integer.valueOf(enumType)) {
               tableDescriptionEntityTmp.setEnums(templateItemBean.getEnums());
           }
           tableDescriptionEntityMapper.insertTableDescription(number, templateItemBean.getItemName(),
                   templateItemBean.getItemDesc(), templateItemBean.getType(),
                   templateItemBean.getIsNull(), tableDescriptionEntityTmp.getEnums(),
                   templateItemBean.getIsUnionOnly(), templateItemBean.getIsSearch(),
                   templateId,templateItemBean.getIsSort());
       }
       //新增模板汇总统计表对应记录
       templateAddBean.setId(templateId);
       AddOrEditTemplateCollect("add", templateAddBean, userId);
       return templateId;
   }
