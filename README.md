# README

## The game

The game plays until there are no more cards in the deck(s).
This means that games with a lot of decks can go for a large number of 
rounds before declaring a winner, but should still complete
near-instantaneously.

## Dependencies
There's a few dependencies. They aren't strictly necessary, but cleanup
the code slightly.

* Enumeratum — My preferred Enum library as its nicer to work with
and type-safe
* scalaTest — "Standard" testing library
* atto — Used for parsing text from CLI. Input is simple enough
this could have been done "by hand", but seemed neater to pull a library
* cats — Already a transitive dependency of "atto", so I used it
in a couple of places as it provides a standard interface
to some operations.
