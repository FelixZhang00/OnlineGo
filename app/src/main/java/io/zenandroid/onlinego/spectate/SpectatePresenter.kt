package io.zenandroid.onlinego.spectate

import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import io.zenandroid.onlinego.model.ogs.GameList
import io.zenandroid.onlinego.ogs.OGSService

/**
 * Created by alex on 05/11/2017.
 */
class SpectatePresenter(val view: SpectateContract.View, val service: OGSService) : SpectateContract.Presenter {

    private val subscriptions = CompositeDisposable()

    override fun subscribe() {
        subscriptions.add(
                service.fetchGameList()
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread()) // TODO: remove me!!!
                        .subscribe(this::setGames)
        )
    }

    private fun setGames(games: GameList) {
        games.results?.forEachIndexed { index, game ->
            val gameConnection = service.connectToGame(game.id)
            subscriptions.add(gameConnection)
            subscriptions.add(gameConnection.gameData
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread()) // TODO: remove me!!!
                    .subscribe({ gameData -> view.setGameData(index, gameData) }))
            subscriptions.add(gameConnection.moves
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread()) // TODO: remove me!!!
                    .subscribe({ move -> view.doMove(index, move) }))
        }

        view.games = games
    }

    override fun unsubscribe() {
        subscriptions.clear()
    }
}