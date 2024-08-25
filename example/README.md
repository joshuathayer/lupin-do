# lupin-do example

## running

Install [just](https://github.com/casey/just). And [shadow-cljs](https://shadow-cljs.github.io/docs/UsersGuide.html#_installation). Then,

```
just example-app
```

Eventually, visit `index.html` in your browser.

## discussion

Take a look in `src/example.cljs`.

Very briefly, see that `render` is pure function of `state` (a Clojure map) to [hiccup](https://github.com/weavejester/hiccup). Note that javascript events are expressed a particular way: `onClick` is a map of `{:on {:click [:some :data]}}`. See [replicant](https://github.com/cjohansen/replicant?tab=readme-ov-file#data-driven-hooks-and-events)'s docs for more on that! Your render function should use the state to generate the full interface to your application. In the example, we display the current value of a counter, as well a use the `:loading-random` value to control whether a message is displayed or not.

Then, there's a `dispatch` function, which accepts these arguments:

```
[state replicant-data handler-data dispatch-fn]
```

* `state` is the entire state of your application, expressed as a Clojure map
* `replicant-data` and `handler-data` come from the Replicant library, and represent events triggered in the DOM. The first value contains the JS node the event happened on, and the second is the user-provided data specified in the `:on` attribute of the element
* `dispatch-fn` is a function that can be used to trigger your dispatch function. This is meant to be passed to functions called by your dispatcher which themselves will need to trigger some state update in your application: AJAX requests, most obviously.

Your `dispatch` function is expected to return a tuple of:

* an updated application state, as a Clojure map
* any events to be re-dispatched after the application state is updated

The example app doesn't demonstrate any use of the latter. In most cases, this will be `nil`, but is useful when you want to update state and then want to trigger an async action (like an AJAX request) that needs to use the updated state. Add the event to the result tuple, then `dispatch` will be called again with that event and your updated state.

If the state map is updated, your `render` function is called with the new value, and the resulting hiccup is displayed to the user. Replicant makes that DOM update efficient.
