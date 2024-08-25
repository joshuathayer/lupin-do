# lupin-do!

`lupin-do!` is a tiny (like 40 loc) frontend library for building web applications. It's inspired by [re-frame](https://day8.github.io/re-frame/re-frame/), but is smaller and does not have a React dependency. Rather, it wraps the very cool [replicant](https://github.com/cjohansen/replicant) library, manages state with a reactive atom, and provides mechanisms for updating state.

### what's with the name?

My two-year-old is named Lupin, and insists on doing things himself by exclaiming "Lupin do!!". And it's a pun on "loop".

### how's it work?

`lupin-do!` is used by defining two pure functions:

* A `render` function which accepts a map which contains the full state of your application, and returns hiccup representing the entirety of your application.
* A `dispatch` function which accepts a tuple representing some event, a map representing the full state of your application, and a function to be called if events need to be send asynchronously, and returns a tuple of the updated application state plus any new events to dispatch immediately.

And that's it. Once those functions are defined, you mount your application (in this case, to the element with id `#lupin-app`) with

```
    (lupin-do! :lupin-app render-fn dispatch-fn)
```

### An example

See `./example` for a simple app that demonstrates basic use of `lupin-do`, including asynchronous event generation as the result of an AJAX request.


### WIP

This is a work in progress. I'm not entirely happy with how async events are handled, and I have no idea how this scales beyond the small projects I've used it on, either from an efficiency or complexity standpoint.
