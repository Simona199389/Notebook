const express = require('express');
const sqlite3 = require('sqlite3').verbose();
const fs = require('fs');
const path = require('path');

const router = express.Router();

const databasePath = './database/database.db';
const databaseFolder = path.dirname(databasePath);

if (!fs.existsSync(databaseFolder)) {
    fs.mkdirSync(databaseFolder, { recursive: true });
}

const db = new sqlite3.Database(databasePath);

db.serialize(() => {
    db.run(
        'CREATE TABLE IF NOT EXISTS notes (ID INTEGER PRIMARY KEY AUTOINCREMENT, Title text not null, Subtitle text not null, Description text not null, Date text not null )'
    );
});

router.get('/', (req, res) => {
    res.send('Welcome to my API!');
});



router.get('/api/notes/', (req, res) => {
    console.log("Select all method")
    db.all('SELECT * FROM notes', [], (err, rows) => {
        if (err) {
            console.error(err.message);
            return res.status(500).json({
                error: 'Failed to retrieve notes items',
            });
        }
        res.json(rows);
    });
});

router.get('/api/notes/:id', (req, res) => {
    console.log("Select method by id")
    const { id } = req.params;
    console.log("Selected note id: "+id);
    db.get('SELECT * FROM notes WHERE ID = ?', [id], (err, row) => {
        if (err) {
            console.error(err.message);
            return res.status(500).json({
                error: 'Failed to select the note',
            });
        }

        if (!row) {
            return res.status(404).json({
                error: 'Note not found',
            });
        }

        res.json({
            message: 'Note selected successfully',
            note: row
        });
    });
});


router.delete('/api/notes/delete/:id', (req, res) => {
    console.log("Delete method")
    const { id } = req.params;
    db.run('DELETE FROM notes WHERE ID = ?', [id], function (err) {
        if (err) {
            console.error(err.message);
            return res.status(500).json({
                error: 'Failed to delete the note',
            });
        }

        if (this.changes === 0) {
            return res.status(404).json({
                error: 'Note not found',
            });
        }

        res.json({
            message: 'Note deleted successfully',
            deletedId: id
        });
    });
});

router.post('/api/notes/add/', (req, res) => {
    console.log("Create method")
    const { Title, Subtitle, Description,ID, Date } = req.body;

    db.run(
        'INSERT INTO notes(Title, Subtitle, Description,ID, Date) VALUES (?, ?, ?, ?, ?)',
        [Title, Subtitle, Description,ID, Date],
        function (err) {
            if (err) {
                console.error(err.message);
                return res.status(500).json({
                    error: 'Failed to add note item',
                });
            }
            res.json({
                ID,
                Title,
                Subtitle,
                Description,
                Date
            });
        }
    );
});

router.put('/api/notes/update', (req, res) => {   
    console.log("Update method")
    const { ID, Title, Subtitle, Description, Date } = req.body;
    console.log('Received request to update note:', req.body);

    if (!ID || !Title || !Subtitle || !Description || !Date) {
        return res.status(400).json({ error: 'Missing required fields' });
    }

    db.run(
        'UPDATE notes SET Title = ?, Subtitle = ?, Description = ?, Date = ? WHERE ID = ?',
        [Title, Subtitle, Description, Date, ID],
        function (err) {
            if (err) {
                console.error('Error updating note:', err.message);
                return res.status(500).json({ error: 'Failed to update the note' });
            }

            if (this.changes === 0) {
                console.log('Note not found:', ID);
                return res.status(404).json({ error: 'Note not found' });
            }

            console.log('Note updated successfully:', ID);
            res.json({ message: 'Note updated successfully', updatedNote: { ID, Title, Subtitle, Description, Date } });
        }
    );
});

module.exports = router;
