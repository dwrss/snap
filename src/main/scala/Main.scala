import io.ConsoleIOHandler
import game.GameRunner
import model._


object Main {
	def main(args: Array[String]): Unit = {
		val gameRunner = GameRunner(ConsoleIOHandler)
		val result = gameRunner.run(NotStarted)
		def getWinnerString(player: Player) = s"Player ${player.id} (${player.stack.size} cards)"
		result match {
			case Finished(winners, _) if winners.sizeIs > 1 =>
				ConsoleIOHandler.display(s"Draw between: \n${winners.map(getWinnerString).mkString("\n")}.")
			case Finished(winners, _) =>
				val winningPlayer = winners.head
				ConsoleIOHandler.display(s"Player ${winningPlayer.id} won (${winningPlayer.stack.size} cards)!")
			case Aborted =>
				ConsoleIOHandler.display(s"Game exited")
		}
	}
}
