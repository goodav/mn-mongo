#!/bin/bash
echo "Waiting for startup.."
until curl http://mongo1:27017/serverStatus\?text\=1 2>&1 | grep uptime | head -1; do
  printf '.'
  sleep 1
done

echo curl http://mongo1:27017/serverStatus\?text\=1 2>&1 | grep uptime | head -1
echo "Started.."

sleep 10

echo SETUP.sh time now: `date +"%T" `
mongo --host mongo1:27017 <<EOF
   var cfg = {
        "_id": "dave",
        "version": 1,
        "members": [
            {
                "_id": 0,
                "host": "mongo1:27017",
                "priority": 2
            },
            {
                "_id": 1,
                "host": "mongo2:27017",
                "priority": 0
            },
            {
                "_id": 2,
                "host": "mongo3:27017",
                "priority": 0,
                "arbiterOnly": true
            }
        ]
    };
    rs.initiate(cfg, { force: true });
    rs.reconfig(cfg, { force: true });
    rs.secondaryOk();
    db.getMongo().setReadPref('primary');
    db.getMongo().setSecondaryOk();
EOF

#until curl http://mongo2:27017/serverStatus\?text\=1 2>&1 | grep uptime | head -1; do
#  printf '.'
#  sleep 1
#done

#echo curl http://mongo2:27017/serverStatus\?text\=1 2>&1 | grep uptime | head -1
#echo "Started.."

#sleep 10
#mongo --host mongo2:27017 <<EOF
#    rs.secondaryOk();
#EOF

tail -f /dev/null
