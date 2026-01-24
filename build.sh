
# 定义目标目录变量
PLUGIN_DIR="/home/zhongming/mc-server-plugins/plugins"
PLUGIN_JAR_NAME="zm-mc-plugin-1.0-SNAPSHOT.jar"

echo "Starting build process..."
mvn clean install

echo "Build completed."
echo "Copying plugin to $PLUGIN_DIR"

# 复制生成的 JAR 包到目标目录
cp target/$PLUGIN_JAR_NAME $PLUGIN_DIR