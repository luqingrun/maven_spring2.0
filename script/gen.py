# -*- coding:utf-8 -*-

import MySQLdb
import os
import sys
from jinja2 import Environment
from jinja2 import FileSystemLoader
from argparse import ArgumentParser

p = ArgumentParser(usage='gen entity,dao,service code from db table, example -db=gsb -t=role,ucUser ',
                   description='this is a code gen')
p.add_argument('-db', default="csb", type=str, help="db")
p.add_argument('-t', default="%", type=str, required=False, help='-t=role,ucUser')
args, remaining = p.parse_known_args(sys.argv)

connect = MySQLdb.connect(host="192.168.16.189", user="write", passwd="$^8C9fvmqzCiK$$q", db="information_schema",
                          charset="utf8")
db = 'csb'


def get_column(table_name):
    cursor = connect.cursor(cursorclass=MySQLdb.cursors.DictCursor)
    cursor.execute("select * from COLUMNS where TABLE_SCHEMA = '%s' and TABLE_NAME = '%s'" % (db, table_name))
    data = cursor.fetchall()
    cursor.close()
    assert len(data) > 0
    return data


def get_tables(t_name):
    cursor = connect.cursor(cursorclass=MySQLdb.cursors.DictCursor)
    table_array = []
    cursor.execute("select * from TABLES where TABLE_SCHEMA = '%s' and TABLE_NAME like '%s'" % (db, t_name))
    data = cursor.fetchall()
    for row in data:
        table_array.append(row['TABLE_NAME'])
    return table_array


def get_java_field(t_name):
    cloumns = get_column(t_name)
    new_cloumns = []
    for row in cloumns:
        f_name = row['COLUMN_NAME']
        f_type = row['DATA_TYPE']
        f_comment = row['COLUMN_COMMENT']
        new_row = {'f_name': get_java_name(f_name), 'f_setget_name': get_class_name(f_name),
                   'f_comment': get_java_name(f_comment),
                   'f_type': get_java_type(f_type), 'f_sql_type': get_sql_type(f_type)}
        new_cloumns.append(dict(new_row.items() + row.items()))
    return new_cloumns


def get_java_type(db_type):
    if 'char' in db_type.lower() or 'text' in db_type.lower():
        return 'String'
    elif 'bigint' in db_type.lower():
        return 'Long'
    elif 'int' in db_type.lower():
        return 'Integer'
    elif 'date' in db_type or 'time' in db_type.lower():
        return 'Date'
    elif 'float' in db_type.lower():
        return 'Float'
    elif 'double' in db_type.lower():
        return 'Double'
    else:
        return db_type


def get_sql_type(db_type):
    if 'char' in db_type.lower() or 'text' in db_type.lower():
        return 'String'
    elif 'bigint' in db_type.lower():
        return 'Long'
    elif 'int' in db_type.lower():
        return 'Int'
    elif 'date' in db_type or 'time' in db_type.lower():
        return 'Timestamp'
    elif 'float' in db_type.lower():
        return 'Float'
    elif 'double' in db_type.lower():
        return 'Double'
    else:
        return db_type


def get_java_name(c_name):
    if "_" in c_name:
        idx = str(c_name).index("_")
        if idx == len(c_name):
            return str(c_name).replace("_", "")
        else:
            replace_old = "_" + str(c_name)[idx + 1]
            replace_new = str(c_name)[idx + 1].upper()
            tmp_c_name = str(c_name).replace(replace_old, replace_new)
            if "_" in tmp_c_name:
                return get_java_name(tmp_c_name)
            else:
                return tmp_c_name
    else:
        return c_name


def get_class_name(t_name):
    java_name = get_java_name(t_name)
    return java_name[0].upper() + java_name[1:]


def get_module_name(t_name):
    if t_name.startswith("uc_"):
        return 'uc'

    if t_name.startswith("sys_"):
        return 'sys'

    if t_name.startswith("order_"):
        return 'order'

    if t_name.startswith("so_"):
        return 'order'

    if t_name.startswith("bd_"):
        return 'sys'

    if t_name.startswith("cms_"):
        return 'sys'

    if t_name.startswith("prod_"):
        return 'product'

    if t_name.startswith("crm_"):
        return 'crm'

    return get_java_name(t_name).lower()


def get_file_path(module_name):
    return 'gongsibao-' + module_name


def get_insert_column(t_name):
    data = get_column(t_name)
    insert_column = ''
    for row in data:
        if 'PRI' == row['COLUMN_KEY']:
            continue
        f_name = row['COLUMN_NAME']
        if len(insert_column) == 0:
            insert_column = "`" + f_name + "`"
        else:
            insert_column += ", `" + f_name + "`"
    return " " + insert_column + " "


def gen_code(table_name):

    root_path = "/Users/luqingrun/IdeaProjects/caishuibao/"
    env = Environment(loader=FileSystemLoader("/Users/luqingrun/PycharmProjects/test/com/gongsibao/"))

    param = dict()
    param['insert_column'] = get_insert_column(table_name)
    param["java_name"] = get_java_name(table_name)
    param['class_name'] = get_class_name(table_name)
    param['pack_name'] = get_class_name(table_name).lower()
    param['columns'] = get_java_field(table_name)
    param['table_name'] = table_name
    param['module_name'] = get_module_name(table_name)

    source_file = root_path + "caishuibao-service/src/main/java/cn/caishuibao/module/"

    entity_path = source_file + "/" + get_module_name(table_name) + "/" + get_java_name(table_name).lower() + "/entity/"
    dao_path = source_file + "/" + get_module_name(table_name) + "/" + get_java_name(table_name).lower() + "/dao/"
    service_path = source_file + "/" + get_module_name(table_name) + "/" + get_java_name(table_name).lower() + "/service/"
    serviceimpl_path = source_file + "/" + get_module_name(table_name) + "/" + get_java_name(table_name).lower() + "/service/impl/"
    controller_path = root_path + get_file_path(get_module_name(table_name)) + "/src/main/java/cn/caishuibao/"+ get_module_name(table_name) +"/controllers/" + get_java_name(
        table_name).lower() + "/"


    controller_path = root_path + "caishuibao-admin" + "/src/main/java/cn/caishuibao/admin/controllers/" + get_module_name(table_name) + get_java_name(table_name).lower() + "/"


    if not os.path.exists(entity_path):
        os.makedirs(entity_path)
    if not os.path.exists(dao_path):
        os.makedirs(dao_path)
    if not os.path.exists(service_path):
        os.makedirs(service_path)
    if not os.path.exists(serviceimpl_path):
        os.makedirs(serviceimpl_path)
    if not os.path.exists(controller_path):
        os.makedirs(controller_path)

    template = env.get_template("entity.tpl")
    java_class = template.stream(param)
    java_class.dump(entity_path + get_class_name(table_name) + ".java", "UTF-8")

    template = env.get_template("dao.tpl")
    java_class = template.stream(param)
    java_class.dump(dao_path + get_class_name(table_name) + "Dao.java", "UTF-8")

    template = env.get_template("service.tpl")
    java_class = template.stream(param)
    java_class.dump(service_path + get_class_name(table_name) + "Service.java", "UTF-8")

    template = env.get_template("serviceimpl.tpl")
    java_class = template.stream(param)
    java_class.dump(serviceimpl_path + get_class_name(table_name) + "ServiceImpl.java", "UTF-8")

    template = env.get_template("controller.tpl")
    java_class = template.stream(param)
    java_class.dump(controller_path + get_class_name(table_name) + "Controller.java", "UTF-8")


if __name__ == '__main__':

    args = p.parse_args()
    db = args.db
    table_names = args.t
    if "," in table_names:
        table_name_array = table_names.split(",")
        for table_name in table_name_array:
            for t_ in get_tables(table_name):
                gen_code(t_)
    else:
        for t_ in get_tables(table_names):
            gen_code(t_)
