# Plutus, a personal finance management application


# Mandatory *-0-*

### Transaction

A transaction is a financial operation that can be an income or an expense. It includes the following information:

- A free text <0>. (DONE)
- A date corresponding to the time the transaction took place <0>.
- Tags <0>. These are keywords that may allow us to classify and search for the transaction more quickly.
- An amount <0>.  (DONE)
- It should be possible to create a new transaction from scratch or to copy an existing transaction that we modify <0>.  (DONE)


### Label

A label is used to categorise a transaction. The user can assign free keywords as <0> tags; however tags can have a specific format to give them a particular semantics:

- A tag beginning with - (e.g. -food, -car) indicates a category of expenditure
- A tag starting with + (e.g. +wage, +interest) indicates an income category
- A tag beginning with = (e.g. =standard) indicates the account on which the transaction takes place; it is also possible to specify two accounts at once (e.g. =standard-savings) to mark a transaction that is a transfer between two accounts

### Storing and saving the account book

The account book will be stored in the form of a SQLite <0> database which can be operated directly or through an ORM (such as Room).  (DONE)


### Graphical interface

#### Creating and selecting account books

Several account books can be hosted by the application, it must be possible to choose the current book as well as to create, duplicate or delete an existing book <0>.  (DONE)

#### Transaction creation, viewing and editing

It shall be necessary to be able to write free text regarding the transaction <0>, the amount <0> and labels <0>.  (DONE)

#### Transaction search and reporting

Transactions must be searchable by criteria that are label <0>. To help us, we can view a list of all the tags used in the <0> book (to search on them).


It is possible to record typical criteria (e.g. food expenditure transactions with the -food label carried out in 2022 within 2 km of Champs-sur-Marne involving an amount of less than 20 euros) in order to quickly re-launch <0> searches.

The search should lead to the list of transactions but also to synthetic reports on these transactions:

- Sum of the amounts of the transactions selected by the search (which makes it possible to obtain the balance for an account) <0>
    