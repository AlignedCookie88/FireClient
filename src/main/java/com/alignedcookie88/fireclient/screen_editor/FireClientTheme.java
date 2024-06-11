package com.alignedcookie88.fireclient.screen_editor;

import imgui.ImGui;
import imgui.flag.ImGuiCol;
import xyz.breadloaf.imguimc.interfaces.Theme;

public class FireClientTheme implements Theme {
    @Override
    public void preRender() {
        ImGui.styleColorsDark();
//        ImGui.popStyleColor(ImGuiCol.FrameBgActive);
//        ImGui.pushStyleColor(ImGuiCol.FrameBgActive, 255, 90, 0, 255);
//        ImGui.popStyleColor(ImGuiCol.FrameBgHovered);
//        ImGui.pushStyleColor(ImGuiCol.FrameBgHovered, 255, 165, 0, 255);
//        ImGui.popStyleColor(ImGuiCol.Button);
//        ImGui.pushStyleColor(ImGuiCol.Button, 255, 90, 0, 255);
//        ImGui.popStyleColor(ImGuiCol.ButtonActive);
//        ImGui.pushStyleColor(ImGuiCol.ButtonActive, 255, 165, 0, 255);
//        ImGui.popStyleColor(ImGuiCol.ButtonHovered);
//        ImGui.pushStyleColor(ImGuiCol.ButtonHovered, 255, 165, 0, 255);
//        ImGui.popStyleColor(ImGuiCol.SliderGrab);
//        ImGui.pushStyleColor(ImGuiCol.SliderGrab, 255, 90, 0, 255);
    }

    @Override
    public void postRender() {

    }
}
