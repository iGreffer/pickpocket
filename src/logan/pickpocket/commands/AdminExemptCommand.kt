package logan.pickpocket.commands;

import logan.api.command.BasicCommand
import logan.api.command.SenderTarget
import logan.pickpocket.config.MessageConfiguration
import logan.pickpocket.main.Profiles
import org.bukkit.Bukkit
import org.bukkit.entity.Player

class AdminExemptCommand : BasicCommand<Player>(
    name = "exempt",
    argRange = 0..1,
    argTypes = listOf(String::class),
    parentCommand = "admin",
    target = SenderTarget.PLAYER,
    permissionNode = "pickpocket.admin.exempt",
    usage = """
        /pickpocket admin exempt - Exempt yourself from being pick-pocketed.
        /pickpocket admin exempt <name> - Exempt another player from being pick-pocketed.
    """.trimIndent()
) {
    override fun run(sender: Player, args: Array<out String>, data: Any?): Boolean {

        if (args.isEmpty()) {
            val profile = Profiles.get(sender)
            val exemptStatus = !profile.profileConfiguration.exemptSectionValue
            profile.profileConfiguration.setExemptSection(exemptStatus)
            sender.sendMessage(MessageConfiguration.getExemptStatusChangeMessage(exemptStatus))
        } else {
            val otherPlayer = Bukkit.getPlayer(args[0]) ?: run {
                sender.sendMessage(MessageConfiguration.getPlayerNotFoundMessage())
                return true
            }
            val profile = Profiles.get(otherPlayer)
            val exemptStatus = !profile.profileConfiguration.exemptSectionValue
            profile.profileConfiguration.setExemptSection(exemptStatus)
            sender.sendMessage(MessageConfiguration.getExemptStatusChangeOtherMessage(otherPlayer, exemptStatus))
            otherPlayer.sendMessage(MessageConfiguration.getExemptStatusChangeMessage(exemptStatus))
        }
        return true
    }
}