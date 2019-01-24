function App() {
    var images = [];
    $.get('/api/images', function (data) {
        console.log(data);
    });

    return <div className="container">
        <div className="row">
            <div className="col">{images}</div>
        </div>
    </div>
}

ReactDOM.render(
    <App />,
    document.getElementById('app')
);
