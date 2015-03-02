# maru me

Inspired by the `pug me` service. Serves up a random maru gif.

Just `<img src="http://marume.herokuapp.com/random.gif">` anywhere you need a maru:

![maru](http://marume.herokuapp.com/random.gif)

Browsers rudely cache 302 redirects on image tags, so you may need to append a query string (e.g. /random.gif?unique-token) if you want more than one random maru on a page.

## Hubot
Add Maru to your chat bot: [maru me](https://github.com/paulhenrich/marume-bot)

##Contribute
1. Fork [this repository](https://github.com/paulhenrich/marume-server)
2. add maru gifs, 1 per line, to `resources/gifs.txt`
3. `lein test`
4. Open pull request

---

A Heroku web app using Compojure.

Thanks to this [maru tumblr](http://meowmeowmaru.tumblr.com/) for most of the initial urls.
