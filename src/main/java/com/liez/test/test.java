package com.liez.test;

import java.text.DecimalFormat;

/**
 * liez
 *
 * @date 2022/3/30
 */
public class test {
    //public static void main(String[] args) throws Exception {
    //    String privateKey="MIICdgIBADANBgkqhkiG9w0BAQEFAASCAmAwggJcAgEAAoGBAKyGktsqSQ7exOQOG+qTMnorkf0K7lG3RnHrXjmH9RyW2EG6YBjhCuZZRsJfs6p3Oebji0cPMbxkkA9Nzv7vKTVfTV3zpVNunLKCXIQhpHjoeOPp1/7GUPWzXVyrKgz2VJTp6guXUPVx8wRn4M1SgTdddrGKSZnAdyjQ+8CkXcSHAgMBAAECgYAlxrm1P4cLuH+IKAa40E5HLe8jZxdT0TudihnxovFqtyTXD4YnLG+EjJvPx8DBXKXTBpPilVMgmyxoDXN7osZ0yYKm3k+Xzl7TQcwcatObOess+wbLrqR1N21lVseFnBrFYw7cTz6nRfjg29kZOV73IyfL6xtdxBADAa2IVyJlkQJBAP+NEWv6Qyi4TDjQI7HwuhJ3CmyjgFiDoxvZ8IyzlV1ruXvQCvzNV8sh9RXK0cpuPY7Pn7vnroKkDPxqIUpvbt0CQQCs1CpnOjUYSFglqoWiHeQO34KI4FA763BHeTRQLz57LoxU8XI0fUqUJ1Fi0f58C/k79TA5gUP8iQTk3V6d7ECzAkAl5M8uib+nrBxxWDvSyPVug4RiM6R2e56WJK0M0BxX6/9nIqRedDyOfIyRHLAbKlmqcZMoE+TGrnjWfQ5OsVNpAkBNCcHhA7w5F75bA9jPe911l8Ha+4ooO3lwEqH0ACssw4IANyP+K787rQ8FKq/yYW/2fwPBDlLc1cLdtaOAZ5grAkEA/cQqn5UXJbK1hmQjHGhqInkegJF81wd9E8pFCeMSAGF9VEg0QhxlOY3KgHtJ0kLXy8piWuLVUOEvc3CvonG5Og==";
    //    String signKey="be241980";
    //    String responseString = "{\n" +
    //            "    \"body\": \"U1qXv8lC9iAPjHKcz4B5r4T4FTNzqZL/wOeN7KWv9mDzMeq0cYqSloj7CLCeirDwJz7a0/1afMebbRFynnAfUmq23hMjjg6YpcWx3YSPqu6zF0BYMXE/oiEXf8GlHvnWNocmbC5yuCcaQ+mL/VX3qn3ERXZQpixd32JybQRECTs=\",\n" +
    //            "    \"head\": {\n" +
    //            "        \"partnerNo\": \"HTIC\",\n" +
    //            "        \"timestamp\": \"2022-07-08 10:00:33\",\n" +
    //            "        \"requestId\": \"3a42c32a-6878-498e-ba5e-abea6227fd90\",\n" +
    //            "        \"version\": \"1.5\",\n" +
    //            "        \"sign\": \"e035bf9cd197b148cfa21a17d94d3e05\",\n" +
    //            "        \"message\": null,\n" +
    //            "        \"resultCode\": 0\n" +
    //            "    }\n" +
    //            "}";
    //    JSONObject responseJson = JSONObject.parseObject(responseString);
    //    RequestResponseBody responseInfo = JSON.toJavaObject(responseJson, RequestResponseBody.class);
    //    String responseStringDecrypt = RSAUtils.checkSignAndDecrypt(responseInfo, signKey, privateKey, "UTF-8", true, true);
    //    JSONObject responseBodyJsonDecrypt = JSONObject.parseObject(responseStringDecrypt);
    //}
    /**

     * 格式化数字为千分位显示；

     * @param 要格式化的数字；

     * @return

     */

    public static String fmtMicrometer(String text)

    {
        DecimalFormat df = null;
        if(text.indexOf(".") > 0)
        {
            if(text.length() - text.indexOf(".")-1 == 0)
            {
                df = new DecimalFormat("###,##0.");

            }else if(text.length() - text.indexOf(".")-1 == 1)
            {
                df = new DecimalFormat("###,##0.0");
            }else
            {
                df = new DecimalFormat("###,##0.00");
            }
        }else
        {
            df = new DecimalFormat("###,##0");
        }
        double number = 0.0;
        try {
            number = Double.parseDouble(text);
        } catch (Exception e) {
            number = 0.0;
        }

        return df.format(number);

    }
    public static void main(String[] args) {
        System.out.println(fmtMicrometer("12345678"));
        String name = "nihao,";
        String split = name.substring(2);
        StringBuilder newName = new StringBuilder("nihao,");
        newName.append("liez");
        System.out.println(newName);
        System.out.println(name);
        System.out.println(split);

    }
}
