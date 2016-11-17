var gulp = require('gulp');
var concat = require('gulp-concat');
var source = require('vinyl-source-stream');
var babelify = require('babelify');
var browserify = require('browserify');
var gutil = require('gulp-util');
var jsBase = './src/main/js';
var targetLocation = './src/main/resources/static/js/code/';


function notify(error) {
    var message = 'In: ';
    var title = 'Error: ';
    if (error.description) {
        title += error.description;
    } else if (error.message) {
        title += error.message;
    }

    if (error.filename) {
        var file = error.filename.split('/');
        message += file[file.length - 1];
    }

    if (error.lineNumber) {
        message += '\nOn Line: ' + error.lineNumber;
    }
    console.log(error);
}


function Bundle() {

     
    var debugType = true;
 

    var Bundler = browserify({
        entries: jsBase+'/main.js',  
        transform: [["babelify", {"presets": ["es2015"]}]],
        extensions: ['.js'],
        debug: debugType,
        cache: {},
        packageCache: {},
        fullPaths: true
    });
    return Bundler
            .bundle()
            .on('error', notify);
}

gulp.task('build', function () {
    Bundle()
            .pipe(source('bundle.js'))
            .pipe(gulp.dest(targetLocation ))
            .on('finish', function ( ) {
                gutil.log("build bundle end");
               
            });
    ;
});







