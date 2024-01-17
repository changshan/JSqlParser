分支管理策略
- “master”--sync jsqlparser/jsqlparser;“smart”--主分支；“feature-[简要介绍]”---日常开发迭代分支
- mvn clean compile -Dmaven.test.skip=false  验证编译，以及用例验证
- mvn package -Dmaven.test.skip=true 生成打包文件
- 解压缩打包文件中source java 到正式项目中

ParserException,JSQLParserException 需要重写