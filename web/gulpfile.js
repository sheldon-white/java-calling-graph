var gulp = require('gulp');
var webserver = require('gulp-webserver');

gulp.task("default", ["webserver"]);
 
gulp.task('webserver', function() {
  gulp.src('.')
    .pipe(webserver({
      port: 8008,
      livereload: true,
      directoryListing: true,
      open: true
    }));
});

