import React, { useEffect } from "react";
import { useState } from "react";
import { TextInput, Text, View, Pressable } from "react-native";
import styles from "./styles";

export function CameraButton({ onPress }) {
    const [isPressed, setIsPressed] = useState(false);
    const [currentColor, setCurrentColor] = useState("white");

    function changeColor() {
        if (isPressed) {
            if (currentColor === "white") {
                setCurrentColor("red");
            } else {
                setCurrentColor("white");
            }
        }
    }

    useEffect(() => {
        setInterval(() => {
            changeColor();
        }, 1000);
    }, []);

    const getButtonStyle = () => {
        if (isPressed) {
            if (currentColor === "white") {
                return styles.buttonCenterInv;
            }
            return styles.buttonCenterRed;
        }
        return styles.buttonCenter;
    };

    return (
        <View style={styles.buttonBorder}>
            <Pressable
                style={getButtonStyle()}
                onPress={() => {
                    console.log("PRESSED");
                    setIsPressed(!isPressed);
                }}
            ></Pressable>
        </View>
    );
}
