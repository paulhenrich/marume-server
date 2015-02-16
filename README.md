# marume

Inspired by the pugme service. Serves up a random maru gif.

##Random Maru demo
Just
`<img src="http://marume.herokuapp.com/random.gif">` anywhere you need a maru. 

![maru](http://marume.herokuapp.com/random.gif)

Browsers rudely cache 302 redirects on image tags, so you may need to put a cache busting query string on the end if you want more than one maru.

## Hubot
Add Maru to your hubot: [maru me](https://github.com/paulhenrich/marume-bot)

##Contribute
1. Fork
2. add maru gifs, 1 per line, to `resources/gifs.txt`
3. `lein test`
4. Open pull request

---

A [Heroku](http://www.heroku.com) web app using Compojure.
