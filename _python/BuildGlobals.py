def hex_to_java_array(hexstr):
    hexstr = hexstr.strip().replace("\n", "")
    bytes_list = []
    for i in range(0, len(hexstr), 2):
        byte = hexstr[i:i+2]
        bytes_list.append(f"(byte)0x{byte}")
    return ", ".join(bytes_list)

def build(path: str):
    name = path.split("/")[-1].split(".")[0]  # babes1

    with open(path, "r",encoding="utf-8") as fp:
        hexdata = fp.read()

    java_array = hex_to_java_array(hexdata)

    with open("src/global/HexPNG.java", "w") as javaFP:
        javaFP.write("public class HexPNG {\n")
        javaFP.write(f"    public static final byte[] {name} = new byte[]{{\n")
        javaFP.write(f"        {java_array}\n")
        javaFP.write("    };\n")
        javaFP.write("}\n")



import os
dir ="resources/images/bin"
for file in os.listdir(dir):
    if file.endswith(".txt"):
        build(dir + "/" +file)