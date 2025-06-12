# 🎵 Playlist Maker - приложение для прослушивания музыки
## 🎧 Медиатека
При запуске приложения открывается экран медиатека. В зависимости от наличия избранных треков отобразится плейсхолдер или список треков
<div align="center">
  <img src="app/screen/Screenshot_media_fav_ph.png" width="300">
  <img src="app/screen/Screenshot_media_fav.png" width="300">
  <br>
  <em>Рис. 1: Экран "Медиатека" избранные треки</em>
</div>

По свайпу вправо откроется вкладка с плейлистами. В зависимости от наличия плейлистов отобразится плейсхолдер или список плейлистов
<div align="center">
  <img src="app/screen/Screenshot_media_playlist_ph.png" width="300">
  <img src="app/screen/Screenshot_media_playlist.png" width="300">
  <br>
  <em>Рис. 2: Экран "Медиатека" плейлисты</em>
</div>

## 💽 Создание плейлиста
Чтобы создать плейлист необходимо нажать на кнопку "Создать плейлист", после чего откроется экран создания плейлиста
<div align="center">
  <img src="app/screen/Screenshot_create.png" width="300">
  <br>
  <em>Рис. 3: Экран "Создать плейлист"</em>
</div>

Обложка плейлиста добавляется пользователем. Если обложка не добавлена, то на месте обложки будет плейсхолдер
<div align="center">
  <img src="app/screen/Screenshot_create1.png" width="300">
  <br>
  <em>Рис. 4: Экран "Создать плейлист" </em>
</div>

Можно посмотреть детали плейлиста:
- Название плейлиста
- Описание плейлиста
- Дата создания плейлиста
- Список треков в плейлисте
- Количество треков в плелисте
- Длительность плейлиста

Кроме того, можно удалить плейлист, изменить или поделиться 

<div align="center">
  <img src="app/screen/Screenshot_playlistDetails.png" width="300">
  <img src="app/screen/Screenshot_playlistDetails1.png" width="300">
  <br>
  <em>Рис. 5: Экран "Детали плейлиста" </em>
</div>


## ⚙️ Настройки
В настройках реализована функция переключения темы приложения
<div align="center">
  <img src="app/screen/Screenshot_settings.png" width="300">
  <img src="app/screen/Screenshot_settings_dark.png" width="300">
  <br>
  <em>Рис. 6: Экран "Настройки"</em>
</div>

## 🔍 Поиск
<div align="center">
  <img src="app/screen/Screenshot_search_tracks.png" width="300">
  <br>
  <em>Рис. 7: Поиск треков</em>
</div>

При нажатии на элемент списка, открывается плеер, где реализованы функции:
- Прослушать трек
- Добавить трек в избранное
- Добавить трек в плейлист

<div align="center">
  <img src="app/screen/Screenshot_player.png" width="300">
  <img src="app/screen/Screenshot_player_addPlaylist.png" width="300">
  <br>
  <em>Рис. 8: Плеер </em>
</div>

При нажатии на кнопку "Очистить историю" история поиска удаляется
<div align="center">
  <img src="app/screen/Screenshot_search_history.png" width="300">
  <img src="app/screen/Screenshot_search_emptyHistory.png" width="300">
  <br>
  <em>Рис. 9: Очистка истории</em>
</div>

## 🛠 Технологии
- Kotlin + Coroutines
- Retrofit и OkHttp
- Room 
- Flow и LiveData
- Koin
- MVVM
- AdapterDelegates
- Clean Architecture
- SharedPreferences



