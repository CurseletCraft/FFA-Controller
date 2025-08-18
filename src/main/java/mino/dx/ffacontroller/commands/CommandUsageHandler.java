package mino.dx.ffacontroller.commands;

import mino.dx.ffacontroller.utils.StringUtil;
import net.j4c0b3y.api.command.actor.Actor;
import net.j4c0b3y.api.command.execution.usage.UsageHandler;
import net.j4c0b3y.api.command.wrapper.CommandHandle;
import net.j4c0b3y.api.command.wrapper.CommandWrapper;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CommandUsageHandler implements UsageHandler {

    @Override
    public List<String> getUsageMessage(Actor actor, CommandHandle commandHandle, String s) {
        return Collections.singletonList("&cUsage: " + getFullUsage(commandHandle, s));
    }

    @Override
    public List<String> getHelpMessage(Actor actor, CommandWrapper commandWrapper, String s, List<String> list) {
        if(list.isEmpty() || !list.getFirst().equals(commandWrapper.getHelp().command())) {
            return null;
        }

        List<String> lines = new ArrayList<>();

        lines.add("&f&m-----------------------------------------------------");
        lines.add("&a" + StringUtil.capitalize(s) + " Commands:");

        for (CommandHandle handle : commandWrapper.getHandles().values()) {
            if (handle.isHidden() || !actor.hasPermission(handle.getPermission())) continue;

            lines.add("&e" + getFullUsage(handle, s) + "&b - " + handle.getDescription());
        }

        lines.add("&f&m-----------------------------------------------------");
        return lines;

    }

    private String getFullUsage(CommandHandle handle, String label) {
        String labelSpace = !handle.getName().isEmpty() ? " " : "";
        String nameSpace = !handle.getUsage().isEmpty() ? " " : "";

        return "/" + label + labelSpace + handle.getName() + nameSpace + handle.getUsage();
    }

}
