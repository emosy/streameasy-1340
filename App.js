import React, { useEffect } from "react";
import { useState } from "react";
import {
    StyleSheet,
    View,
    TouchableOpacity,
    StatusBar,
    PermissionsAndroid,
    Text,
} from "react-native";

import { NodeCameraView } from "react-native-nodemediaclient";

const App = () => {
    const cameraViewRef = React.useRef(null);
    const [hasPermission, setHasPermission] = useState(null);
    const [isStreaming, setIsStreaming] = useState(0);
    const [pressed, setPressed] = useState(false);

    const streamURL = "rtmp://36bay2.tulix.tv/ryaniosdfs/channel1";

    const getButtonColor = () => {
        if (isStreaming != 2001) {
            return styles.buttonCenter;
        }
        return styles.buttonCenterRed;
    };

    const requestCameraPermission = async () => {
        try {
            const granted = await PermissionsAndroid.requestMultiple(
                [
                    PermissionsAndroid.PERMISSIONS.CAMERA,
                    PermissionsAndroid.PERMISSIONS.RECORD_AUDIO,
                ],
                {
                    title: "Cool Photo App Camera And Microphone Permission",
                    message:
                        "Cool Photo App needs access to your camera " +
                        "so you can take awesome pictures.",
                    buttonNeutral: "Ask Me Later",
                    buttonNegative: "Cancel",
                    buttonPositive: "OK",
                }
            );
            if (
                granted["android.permission.CAMERA"] === "granted" &&
                granted["android.permission.RECORD_AUDIO"] === "granted"
            ) {
                console.log("You can use the camera");
                setHasPermission(true);
            } else {
                console.log("Camera permission denied");
                setHasPermission(false);
            }
        } catch (err) {
            console.warn(err);
        }
    };

    useEffect(() => {
        requestCameraPermission();
    }, []);

    return hasPermission ? (
        <View
            style={{
                flex: 1,
                backgroundColor: "#333",
                flexDirection: "row",
                justifyContent: "flex-end",
            }}
        >
            <StatusBar hidden={true} />
            <NodeCameraView
                style={{
                    flex: 1,
                    flexDirection: "column",
                }}
                ref={cameraViewRef}
                camera={{ cameraId: 0, cameraFrontMirror: true }}
                audio={{ bitrate: 32000, profile: 1, samplerate: 44100 }}
                video={{
                    preset: 1,
                    bitrate: 500000,
                    profile: 1,
                    fps: 30,
                    videoFrontMirror: false,
                }}
                smoothSkinLevel={3}
                autopreview={true}
                onStatus={(code, msg) => {
                    console.log("onStatus=" + code + " msg=" + msg);
                    setIsStreaming(code);
                    if (code === 2002) {
                        alert("CONNECTION FAILED");
                    }
                }}
                //outputUrl={"rtmp://192.168.0.247/live/xyz"}
                outputUrl={streamURL}
            />
            <View style={styles.button}>
                <TouchableOpacity
                    style={styles.buttonBorder}
                    onPress={() => {
                        if (isStreaming !== 2001) {
                            cameraViewRef.current.start();
                        } else {
                            cameraViewRef.current.stop();
                        }
                    }}
                >
                    <View style={getButtonColor()} />
                </TouchableOpacity>
            </View>
        </View>
    ) : (
        <Text>Permission Denied</Text>
    );
};

const styles = StyleSheet.create({
    button: {
        display: "flex",
        flex: 1,
        width: 100,
        height: "100%",
        position: "absolute",
        flexDirection: "column",
        justifyContent: "center",
        //backgroundColor: "#FFFFFF",
    },
    buttonBorder: {
        width: 90,
        height: 90,
        backgroundColor: "#FFFFFF",
        borderRadius: 100,
        backgroundColor: "rgba(52, 52, 52, 0)",
        justifyContent: "center",
        alignItems: "center",
        borderColor: "#FFFFFF",
        borderWidth: 3,
        position: "absolute",
        marginTop: 50,
    },
    buttonCenter: {
        width: 75,
        height: 75,
        backgroundColor: "#FFFFFF",
        borderRadius: 100,
    },
    buttonCenterRed: {
        width: 75,
        height: 75,
        backgroundColor: "#FF0004",
        borderRadius: 100,
    },
    buttonCenterInv: { backgroundColor: "rgba(52, 52, 52, 0)" },
});

export default App;
