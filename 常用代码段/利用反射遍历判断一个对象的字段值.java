    /**
     * 获取值如果是Null默认为0
     */
    public static void operateNull(Object object) {
        Class clz = object.getClass();
        try {
            for(Field f : clz.getDeclaredFields()) {
                f.setAccessible(true);
                Object val = f.get(object);
                if(val == null) {
                    f.set(object, new Integer(0));
                }
            }
        }catch (Exception e) {
            e.printStackTrace();
        }
    }